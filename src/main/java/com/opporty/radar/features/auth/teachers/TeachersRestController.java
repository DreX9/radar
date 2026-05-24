package com.opporty.radar.features.auth.teachers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("teachers")
@RequiredArgsConstructor
public class TeachersRestController {
    private final TeachersService teachersService;

    @GetMapping
    public ResponseEntity<List<TeachersViewDTO>> list() {
        List<TeachersViewDTO> list = teachersService.getAllTeachers();
        if (list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No data");
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<TeachersViewDTO> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(teachersService.getTeacherById(id));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No data");
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeachersViewDTO> insertTeacher(@Valid @RequestBody TeachersWriteDTO teacher) {
        try {
            return ResponseEntity.ok(teachersService.addTeacher(teacher));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeachersViewDTO> updateTeacher(@Valid @RequestBody TeachersWriteDTO teacher) {
        try {
            return ResponseEntity.ok(teachersService.updateTeacher(teacher));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteById(@PathVariable long id) {
        try {
            teachersService.deleteTeacherById(id);
            return ResponseEntity.ok(String.format("Teacher deleted with id: %d", id));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
