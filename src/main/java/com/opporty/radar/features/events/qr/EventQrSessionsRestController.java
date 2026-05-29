package com.opporty.radar.features.events.qr;

import com.opporty.radar.features.auth.users.Users;
import com.opporty.radar.features.events.registrations.EventRegistrationsViewDTO;
import com.opporty.radar.security.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("event-qr-sessions")
@RequiredArgsConstructor
public class EventQrSessionsRestController {

    private final EventQrSessionsService eventQrSessionsService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'MANAGER')")
    public ResponseEntity<QrSessionResponse> createSession(@Valid @RequestBody EventQrSessionsWriteDTO dto) {
        Users currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        return ResponseEntity.ok(eventQrSessionsService.generateQrSession(dto, currentUser));
    }

    @PostMapping("/scan")
    public ResponseEntity<EventRegistrationsViewDTO> scan(@Valid @RequestBody QrScanRequest request) {
        Users currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        return ResponseEntity.ok(eventQrSessionsService.scanQrCode(request.token(), currentUser));
    }

    @GetMapping("/active/{eventId}")
    public ResponseEntity<QrSessionResponse> getActiveSession(
            @PathVariable Long eventId,
            @RequestParam String type) {
        QrSessionResponse response = eventQrSessionsService.getActiveSession(eventId, type);
        return ResponseEntity.ok(response);
    }
}
