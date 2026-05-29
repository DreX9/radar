package com.opporty.radar.features.events.registrations;

import com.opporty.radar.features.auth.users.Users;
import com.opporty.radar.features.events.core.Events;
import com.opporty.radar.features.events.core.EventsRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class EventRegistrationsService {

    private final EventRegistrationsRepository eventRegistrationsRepository;
    private final EventsRepository eventsRepository;
    private final EventRegistrationsMapper eventRegistrationsMapper;

    @Transactional(readOnly = true)
    public List<EventRegistrationsViewDTO> getRegistrationsByUser(Users user) {
        return eventRegistrationsRepository.findByUser(user).stream()
                .map(eventRegistrationsMapper::toDt)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventRegistrationsViewDTO> getRegistrationsByEvent(Long eventId) {
        Events event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado con ID: " + eventId));
        return eventRegistrationsRepository.findByEvent(event).stream()
                .map(eventRegistrationsMapper::toDt)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventRegistrationsViewDTO registerUser(EventRegistrationsWriteDTO dto, Users user) {
        // findByIdWithLock: bloqueo pesimista para evitar race condition en el aforo
        Events event = eventsRepository.findByIdWithLock(dto.eventId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado con ID: " + dto.eventId()));

        // Check duplicate registration
        if (eventRegistrationsRepository.existsByUserAndEvent(user, event)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya estás registrado en este evento.");
        }

        // Check event capacity
        if (event.getCapacidad() != null && event.getCapacidad() > 0) {
            long activeCount = eventRegistrationsRepository.countActiveRegistrationsByEvent(event);
            if (activeCount >= event.getCapacidad()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "El aforo de este evento ha sido completado. No hay más lugares disponibles.");
            }
        }

        RoleInEvent role;
        try {
            role = dto.roleInEvent() != null ? RoleInEvent.valueOf(dto.roleInEvent().toUpperCase()) : RoleInEvent.ATTENDEE;
        } catch (Exception e) {
            role = RoleInEvent.ATTENDEE;
        }

        AttendanceStatus initialStatus = event.isRequiresApproval() ? AttendanceStatus.PENDING_APPROVAL : AttendanceStatus.REGISTERED;

        EventRegistrations registration = EventRegistrations.builder()
                .user(user)
                .event(event)
                .attendanceStatus(initialStatus)
                .qrEntryScanned(false)
                .qrExitScanned(false)
                .attendanceCompleted(false)
                .certificateGenerated(false)
                .roleInEvent(role)
                .notes(dto.notes())
                .build();

        EventRegistrations saved = eventRegistrationsRepository.save(registration);
        return eventRegistrationsMapper.toDt(saved);
    }

    @Transactional
    public EventRegistrationsViewDTO updateStatus(Long registrationId, String statusStr) {
        EventRegistrations registration = eventRegistrationsRepository.findById(registrationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro de evento no encontrado con ID: " + registrationId));

        AttendanceStatus status;
        try {
            status = AttendanceStatus.valueOf(statusStr.toUpperCase());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado de asistencia inválido: " + statusStr);
        }

        registration.setAttendanceStatus(status);

        // If status is updated to completed, automatically mark attendance completed
        if (status == AttendanceStatus.COMPLETED) {
            registration.setAttendanceCompleted(true);
        }

        EventRegistrations updated = eventRegistrationsRepository.save(registration);
        return eventRegistrationsMapper.toDt(updated);
    }

    @Transactional
    public EventRegistrationsViewDTO generateCertificate(Long registrationId) {
        EventRegistrations registration = eventRegistrationsRepository.findById(registrationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro de evento no encontrado con ID: " + registrationId));

        if (!registration.isAttendanceCompleted() && registration.getAttendanceStatus() != AttendanceStatus.COMPLETED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede generar certificado si la asistencia no ha sido completada.");
        }

        registration.setCertificateGenerated(true);
        registration.setCertificateUrl("https://radar.opporty.com/certificates/" + registration.getId());

        EventRegistrations updated = eventRegistrationsRepository.save(registration);
        return eventRegistrationsMapper.toDt(updated);
    }
}
