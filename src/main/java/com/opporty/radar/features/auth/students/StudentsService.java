package com.opporty.radar.features.auth.students;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentsService {
    private final StudentsRepository studentsRepository;
    private final StudentsMapper studentsMapper;

    public List<StudentsViewDTO> getAllStudents() {
        return studentsRepository.findAll().stream().map(studentsMapper::toDt).toList();
    }
}
