package com.opporty.radar.features.auth.teachers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("teachers")
@RequiredArgsConstructor
public class TeachersRestController {
    private final TeachersService teachersService;

    @GetMapping
    public ResponseEntity<List<TeachersViewDTO>> list() throws ResponseStatusException {
        List<TeachersViewDTO> list = teachersService.getAllTeachers();
        if (list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No data");
        }
        return ResponseEntity.ok(list);
    }
}
