package com.opporty.radar.features.auth.students;

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
@RequestMapping("students")
@RequiredArgsConstructor
public class StudentsRestController {
    private final StudentsService studentsService;

    @GetMapping
    public ResponseEntity<List<StudentsViewDTO>> list() {
        List<StudentsViewDTO> list = studentsService.getAllStudents();
        if (list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No data");
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<StudentsViewDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(studentsService.getStudentById(id));
    }

    @GetMapping("me")
    public ResponseEntity<StudentsViewDTO> getMe(java.security.Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok(studentsService.getStudentByUsername(username));
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentsViewDTO> insertStudent(@Valid @RequestBody StudentsWriteDTO student) {
        return ResponseEntity.ok(studentsService.addStudent(student));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentsViewDTO> updateStudent(@Valid @RequestBody StudentsWriteDTO student) {
        return ResponseEntity.ok(studentsService.updateStudent(student));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteById(@PathVariable long id) {
        studentsService.deleteStudentById(id);
        return ResponseEntity.ok(String.format("Student deleted with id: %d", id));
    }
}
