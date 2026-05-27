package com.opporty.radar.features.events.core;

import com.opporty.radar.features.auth.users.Users;
import com.opporty.radar.security.SecurityUtils;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("events")
@RequiredArgsConstructor
public class EventsRestController {

    private final EventsService eventsService;

    @GetMapping
    public ResponseEntity<List<EventsViewDTO>> list() {
        List<EventsViewDTO> list = eventsService.getAllEvents();
        if (list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay eventos registrados");
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<EventsViewDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(eventsService.getEventById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'MANAGER')")
    public ResponseEntity<EventsViewDTO> create(@Valid @RequestBody EventsWriteDTO dto) {
        Users currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        return ResponseEntity.ok(eventsService.createEvent(dto, currentUser));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'MANAGER')")
    public ResponseEntity<EventsViewDTO> update(@PathVariable Long id, @Valid @RequestBody EventsWriteDTO dto) {
        Users currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        return ResponseEntity.ok(eventsService.updateEvent(id, dto, currentUser));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'MANAGER')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        eventsService.deleteEventById(id);
        return ResponseEntity.ok(String.format("Evento con ID %d eliminado exitosamente", id));
    }
}
