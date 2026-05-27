package com.opporty.radar.features.events.qr;

import java.time.LocalDateTime;

public record EventQrSessionsViewDTO(
    Long id,
    Long eventId,
    String eventTitulo,
    String type,
    String token,
    LocalDateTime generatedAt,
    LocalDateTime expiresAt,
    boolean active,
    Long createdById,
    String createdByUsername,
    LocalDateTime createdAt
) {}
