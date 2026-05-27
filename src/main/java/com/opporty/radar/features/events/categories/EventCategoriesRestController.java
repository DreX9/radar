package com.opporty.radar.features.events.categories;

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
@RequestMapping("event-categories")
@RequiredArgsConstructor
public class EventCategoriesRestController {

    private final EventCategoriesService eventCategoriesService;

    @GetMapping
    public ResponseEntity<List<EventCategoriesViewDTO>> list() {
        List<EventCategoriesViewDTO> list = eventCategoriesService.getAllCategories();
        if (list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay categorías registradas");
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<EventCategoriesViewDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(eventCategoriesService.getCategoryById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<EventCategoriesViewDTO> create(@Valid @RequestBody EventCategoriesWriteDTO dto) {
        return ResponseEntity.ok(eventCategoriesService.createCategory(dto));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<EventCategoriesViewDTO> update(@PathVariable Long id, @Valid @RequestBody EventCategoriesWriteDTO dto) {
        return ResponseEntity.ok(eventCategoriesService.updateCategory(id, dto));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        eventCategoriesService.deleteCategoryById(id);
        return ResponseEntity.ok(String.format("Categoría con ID %d eliminada exitosamente", id));
    }
}
