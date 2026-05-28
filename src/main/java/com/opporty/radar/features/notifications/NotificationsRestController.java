package com.opporty.radar.features.notifications;

import com.opporty.radar.features.auth.users.Users;
import com.opporty.radar.security.SecurityUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("notifications")
@RequiredArgsConstructor
public class NotificationsRestController {

    private final NotificationsService notificationsService;

    @GetMapping("my-notifications")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotificationsViewDTO>> getMyNotifications() {
        Users currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        return ResponseEntity.ok(notificationsService.getUserNotifications(currentUser));
    }

    @GetMapping("my-notifications/unread-count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> getUnreadCount() {
        Users currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        return ResponseEntity.ok(notificationsService.getUnreadCount(currentUser));
    }

    @PatchMapping("{id}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotificationsViewDTO> markAsRead(@PathVariable Long id) {
        Users currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        return ResponseEntity.ok(notificationsService.markAsRead(id, currentUser));
    }
}
