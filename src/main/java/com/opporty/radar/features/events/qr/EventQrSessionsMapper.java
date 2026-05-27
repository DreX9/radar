package com.opporty.radar.features.events.qr;

import com.opporty.radar.common.MapperInterface;
import com.opporty.radar.features.events.core.Events;
import org.springframework.stereotype.Component;

@Component
public class EventQrSessionsMapper implements MapperInterface<EventQrSessions, EventQrSessionsWriteDTO, EventQrSessionsViewDTO> {

    @Override
    public EventQrSessionsViewDTO toDt(EventQrSessions entity) {
        if (entity == null) {
            return null;
        }
        return new EventQrSessionsViewDTO(
                entity.getId(),
                entity.getEvent() != null ? entity.getEvent().getId() : null,
                entity.getEvent() != null ? entity.getEvent().getTitulo() : null,
                entity.getType().name(),
                entity.getToken(),
                entity.getGeneratedAt(),
                entity.getExpiresAt(),
                entity.isActive(),
                entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null,
                entity.getCreatedBy() != null ? entity.getCreatedBy().getUsername() : null,
                entity.getCreatedAt()
        );
    }

    @Override
    public EventQrSessions toEntity(EventQrSessionsWriteDTO dto) {
        if (dto == null) {
            return null;
        }

        QrSessionType type;
        try {
            type = QrSessionType.valueOf(dto.type().toUpperCase());
        } catch (Exception e) {
            type = QrSessionType.ENTRY;
        }

        return EventQrSessions.builder()
                .event(Events.builder().id(dto.eventId()).build())
                .type(type)
                .active(true)
                .build();
    }
}
