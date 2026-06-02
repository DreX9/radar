package com.opporty.radar.features.notifications;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record NotificationsViewDTO(
        Long id,
        Long userId,
        String title,
        String message,
        @JsonProperty("isRead") boolean isRead,
        Long eventId,
        LocalDateTime createdAt
) {
}
