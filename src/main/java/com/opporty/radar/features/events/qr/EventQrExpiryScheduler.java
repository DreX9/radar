package com.opporty.radar.features.events.qr;

import com.opporty.radar.features.events.core.Estado;
import com.opporty.radar.features.events.core.Events;
import com.opporty.radar.features.events.core.EventsRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Scheduler that automatically marks expired QR sessions as inactive.
 * Runs every 60 seconds to deactivate any QR session whose expiresAt
 * has already passed, preventing them from being used even if the
 * lazy expiry check at scan time is bypassed.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EventQrExpiryScheduler {

    private final EventQrSessionsRepository eventQrSessionsRepository;
    private final EventsRepository eventsRepository;
    private final EventQrSessionsService eventQrSessionsService;

    @Scheduled(fixedRate = 60_000) // every 60 seconds
    @Transactional
    public void deactivateExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();
        List<EventQrSessions> expired = eventQrSessionsRepository.findByActiveTrueAndExpiresAtBefore(now);
        if (!expired.isEmpty()) {
            expired.forEach(session -> session.setActive(false));
            eventQrSessionsRepository.saveAll(expired);
            log.info("[QR Scheduler] Desactivadas {} sesión(es) QR expiradas.", expired.size());
        }
    }

    @Scheduled(fixedRate = 30_000) // every 30 seconds
    @Transactional
    public void autoActivateQrSessions() {
        LocalDateTime now = LocalDateTime.now();

        // 1. Inicio automático de eventos programados (SCHEDULED) al llegar la hora de inicio
        List<Events> scheduledEvents = eventsRepository.findByEstado(Estado.SCHEDULED);
        for (Events event : scheduledEvents) {
            if (event.getFechaInicio() != null && event.getHoraInicio() != null) {
                LocalDateTime startDateTime = LocalDateTime.of(event.getFechaInicio(), event.getHoraInicio());
                if (!now.isBefore(startDateTime)) {
                    event.setEstado(Estado.PUBLISHED);
                    eventsRepository.save(event);
                    log.info("[Event Scheduler] El evento ID: {} ha alcanzado su hora de inicio y ha sido INICIADO (publicado) automáticamente.", event.getId());
                }
            }
        }

        // 2. Activación automática de QRs para eventos publicados (PUBLISHED)
        List<Events> publishedEvents = eventsRepository.findByEstado(Estado.PUBLISHED);
        for (Events event : publishedEvents) {
            if (!event.isAllowQrAttendance()) {
                continue;
            }

            // QR de Entrada: se activa a la hora de inicio por 15 minutos
            if (event.getFechaInicio() != null && event.getHoraInicio() != null) {
                LocalDateTime startDateTime = LocalDateTime.of(event.getFechaInicio(), event.getHoraInicio());
                if (!now.isBefore(startDateTime) && now.isBefore(startDateTime.plusMinutes(15))) {
                    boolean exists = eventQrSessionsRepository.existsByEventAndType(event, QrSessionType.ENTRY);
                    if (!exists) {
                        try {
                            EventQrSessionsWriteDTO dto = new EventQrSessionsWriteDTO(event.getId(), "ENTRY", 15);
                            eventQrSessionsService.generateQrSession(dto, event.getCreatedBy());
                            log.info("[QR Scheduler] Auto-activado QR de INGRESO para el evento ID: {}", event.getId());
                        } catch (Exception e) {
                            log.error("[QR Scheduler] Error al auto-activar QR de INGRESO para evento ID {}: {}", event.getId(), e.getMessage());
                        }
                    }
                }
            }

            // QR de Salida: se activa 5 minutos antes del final por 5 minutos
            if (event.getFechaFin() != null && event.getHoraFin() != null) {
                LocalDateTime endDateTime = LocalDateTime.of(event.getFechaFin(), event.getHoraFin());
                LocalDateTime exitStartDateTime = endDateTime.minusMinutes(5);
                if (!now.isBefore(exitStartDateTime) && now.isBefore(endDateTime)) {
                    boolean exists = eventQrSessionsRepository.existsByEventAndType(event, QrSessionType.EXIT);
                    if (!exists) {
                        try {
                            EventQrSessionsWriteDTO dto = new EventQrSessionsWriteDTO(event.getId(), "EXIT", 5);
                            eventQrSessionsService.generateQrSession(dto, event.getCreatedBy());
                            log.info("[QR Scheduler] Auto-activado QR de SALIDA para el evento ID: {}", event.getId());
                        } catch (Exception e) {
                            log.error("[QR Scheduler] Error al auto-activar QR de SALIDA para evento ID {}: {}", event.getId(), e.getMessage());
                        }
                    }
                }
            }
        }
    }
}
