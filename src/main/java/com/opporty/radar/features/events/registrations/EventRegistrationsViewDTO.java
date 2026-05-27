package com.opporty.radar.features.events.registrations;

import java.time.LocalDateTime;

public record EventRegistrationsViewDTO(
    Long id,
    Long userId,
    String username,
    String userEmail,
    Long eventId,
    String eventTitulo,
    LocalDateTime registeredAt,
    String attendanceStatus,
    boolean qrEntryScanned,
    boolean qrExitScanned,
    LocalDateTime checkInAt,
    LocalDateTime checkOutAt,
    boolean attendanceCompleted,
    boolean certificateGenerated,
    String certificateUrl,
    String roleInEvent,
    String notes,
    LocalDateTime createdAt
) {}
