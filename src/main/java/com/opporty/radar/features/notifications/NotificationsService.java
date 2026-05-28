package com.opporty.radar.features.notifications;

import com.opporty.radar.features.auth.users.Users;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class NotificationsService {

    private final NotificationsRepository notificationsRepository;
    private final NotificationsMapper notificationsMapper;

    @Transactional(readOnly = true)
    public List<NotificationsViewDTO> getUserNotifications(Users user) {
        return notificationsRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(notificationsMapper::toDt)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(Users user) {
        return notificationsRepository.countByUserAndIsReadFalse(user);
    }

    @Transactional
    public NotificationsViewDTO markAsRead(Long notificationId, Users user) {
        Notifications notification = notificationsRepository.findById(notificationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificación no encontrada con ID: " + notificationId));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para modificar esta notificación.");
        }

        notification.setRead(true);
        Notifications saved = notificationsRepository.save(notification);
        return notificationsMapper.toDt(saved);
    }
}
