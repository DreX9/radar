package com.opporty.radar.features.notifications;

import java.time.LocalDateTime;

public record NotificationsViewDTO(
        Long id,
        Long userId,
        String title,
        String message,
        boolean isRead,
        LocalDateTime createdAt
) {
}
