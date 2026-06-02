package com.opporty.radar.features.notifications;

import org.springframework.stereotype.Component;

@Component
public class NotificationsMapper {

    public NotificationsViewDTO toDt(Notifications notification) {
        if (notification == null) {
            return null;
        }

        return new NotificationsViewDTO(
                notification.getId(),
                notification.getUser() != null ? notification.getUser().getId() : null,
                notification.getTitle(),
                notification.getMessage(),
                notification.isRead(),
                notification.getEventId(),
                notification.getCreatedAt()
        );
    }
}
