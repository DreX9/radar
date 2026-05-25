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
import com.opporty.radar.common.TeacherRegisterRequest;
import com.opporty.radar.security.AuthenticationService;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("teachers")
@RequiredArgsConstructor
public class TeachersRestController {
    private final TeachersService teachersService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeachersViewDTO> registerTeacher(@Valid @RequestBody TeacherRegisterRequest request) {
        return ResponseEntity.ok(authenticationService.registerTeacher(request));
    }

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
        return ResponseEntity.ok(teachersService.getTeacherById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeachersViewDTO> insertTeacher(@Valid @RequestBody TeachersWriteDTO teacher) {
        return ResponseEntity.ok(teachersService.addTeacher(teacher));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeachersViewDTO> updateTeacher(@Valid @RequestBody TeachersWriteDTO teacher) {
        return ResponseEntity.ok(teachersService.updateTeacher(teacher));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteById(@PathVariable long id) {
        teachersService.deleteTeacherById(id);
        return ResponseEntity.ok(String.format("Teacher deleted with id: %d", id));
    }
}
