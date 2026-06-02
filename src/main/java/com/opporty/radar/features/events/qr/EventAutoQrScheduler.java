package com.opporty.radar.features.events.qr;

import com.opporty.radar.features.events.core.Events;
import com.opporty.radar.features.events.core.EventsRepository;
import com.opporty.radar.features.notifications.Notifications;
import com.opporty.radar.features.notifications.NotificationsRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventAutoQrScheduler {

    private final EventsRepository eventsRepository;
    private final EventQrSessionsRepository eventQrSessionsRepository;
    private final com.opporty.radar.features.notifications.NotificationsService notificationsService;

    @Scheduled(fixedRate = 60000) // Se ejecuta cada minuto
    @Transactional
    public void generateContingencyQRs() {
        LocalDate today = LocalDate.now();
        LocalTime nowTime = LocalTime.now();

        // Buscar todos los eventos del día de hoy
        List<Events> todaysEvents = eventsRepository.findAll().stream()
                .filter(e -> e.getFechaInicio() != null && e.getFechaInicio().equals(today))
                .toList();

        for (Events event : todaysEvents) {
            // 1. Contingencia para INGRESO (15 minutos antes de la horaInicio)
            if (event.getHoraInicio() != null && !event.isAutoQrEntryGenerated()) {
                long minutesUntilStart = java.time.Duration.between(nowTime, event.getHoraInicio()).toMinutes();
                
                // Si faltan 15 minutos o menos (y el evento no ha pasado hace más de 1 hora)
                if (minutesUntilStart <= 15 && minutesUntilStart > -60) {
                    generateSession(event, QrSessionType.ENTRY);
                    event.setAutoQrEntryGenerated(true);
                    eventsRepository.save(event);
                    
                    notificationsService.createNotification(event.getCreatedBy(), "⚠️ Evento próximo a iniciar", 
                            "El QR de INICIO para el evento '" + event.getTitulo() + "' se ha autogenerado. Puedes suspenderlo si hay algún inconveniente.",
                            event.getId());
                    log.info("[AutoQR] QR de INICIO generado para evento ID: {}", event.getId());
                }
            }

            // 2. Contingencia para SALIDA (15 minutos antes de la horaFin)
            if (event.getHoraFin() != null && !event.isAutoQrExitGenerated()) {
                long minutesUntilEnd = java.time.Duration.between(nowTime, event.getHoraFin()).toMinutes();
                
                // Si faltan 15 minutos o menos (y el evento no ha pasado hace más de 1 hora)
                if (minutesUntilEnd <= 15 && minutesUntilEnd > -60) {
                    generateSession(event, QrSessionType.EXIT);
                    event.setAutoQrExitGenerated(true);
                    eventsRepository.save(event);
                    
                    notificationsService.createNotification(
                    event.getCreatedBy(),
                    "🟢 Evento próximo a finalizar",
                    "El QR de SALIDA para el evento '" + event.getTitulo() + "' se ha autogenerado.",
                    event.getId()
            );        log.info("[AutoQR] QR de SALIDA generado para evento ID: {}", event.getId());
                }
            }
        }
    }

    private void generateSession(Events event, QrSessionType type) {
        // Desactivar sesiones activas previas del mismo tipo
        List<EventQrSessions> activeSessions = eventQrSessionsRepository.findByEventAndTypeAndActiveTrue(event, type);
        for (EventQrSessions activeSession : activeSessions) {
            activeSession.setActive(false);
        }
        eventQrSessionsRepository.saveAll(activeSessions);

        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(15); // Validez estricta de 15 minutos

        EventQrSessions session = EventQrSessions.builder()
                .event(event)
                .type(type)
                .token(token)
                .generatedAt(now)
                .expiresAt(expiresAt)
                .active(true)
                .createdBy(event.getCreatedBy()) // El organizador del evento
                .build();

        eventQrSessionsRepository.save(session);
    }


}
