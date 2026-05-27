package com.opporty.radar.features.events.registrations;

import com.opporty.radar.common.MapperInterface;
import com.opporty.radar.features.events.core.Events;
import org.springframework.stereotype.Component;

@Component
public class EventRegistrationsMapper implements MapperInterface<EventRegistrations, EventRegistrationsWriteDTO, EventRegistrationsViewDTO> {

    @Override
    public EventRegistrationsViewDTO toDt(EventRegistrations entity) {
        if (entity == null) {
            return null;
        }

        return new EventRegistrationsViewDTO(
                entity.getId(),
                entity.getUser() != null ? entity.getUser().getId() : null,
                entity.getUser() != null ? entity.getUser().getUsername() : null,
                entity.getUser() != null ? entity.getUser().getEmail() : null,
                entity.getEvent() != null ? entity.getEvent().getId() : null,
                entity.getEvent() != null ? entity.getEvent().getTitulo() : null,
                entity.getRegisteredAt(),
                entity.getAttendanceStatus().name(),
                entity.isQrEntryScanned(),
                entity.isQrExitScanned(),
                entity.getCheckInAt(),
                entity.getCheckOutAt(),
                entity.isAttendanceCompleted(),
                entity.isCertificateGenerated(),
                entity.getCertificateUrl(),
                entity.getRoleInEvent().name(),
                entity.getNotes(),
                entity.getCreatedAt()
        );
    }

    @Override
    public EventRegistrations toEntity(EventRegistrationsWriteDTO dto) {
        if (dto == null) {
            return null;
        }

        RoleInEvent role;
        try {
            role = RoleInEvent.valueOf(dto.roleInEvent().toUpperCase());
        } catch (Exception e) {
            role = RoleInEvent.ATTENDEE;
        }

        return EventRegistrations.builder()
                .event(Events.builder().id(dto.eventId()).build())
                .roleInEvent(role)
                .notes(dto.notes())
                .attendanceStatus(AttendanceStatus.REGISTERED)
                .build();
    }
}
