package com.opporty.radar.features.events.tags;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("tags")
@RequiredArgsConstructor
public class TagsRestController {

    private final TagsService tagsService;

    @GetMapping
    public ResponseEntity<List<TagsViewDTO>> list() {
        List<TagsViewDTO> list = tagsService.getAllTags();
        if (list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay tags registrados");
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<TagsViewDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tagsService.getTagById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<TagsViewDTO> create(@Valid @RequestBody TagsWriteDTO dto) {
        return ResponseEntity.ok(tagsService.addTag(dto));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        tagsService.deleteTagById(id);
        return ResponseEntity.ok(String.format("Tag con ID %d eliminado exitosamente", id));
    }
}
