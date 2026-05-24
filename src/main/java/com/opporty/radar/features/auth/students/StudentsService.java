package com.opporty.radar.features.auth.students;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentsService {
    private final StudentsRepository studentsRepository;
    private final StudentsMapper studentsMapper;

    public List<StudentsViewDTO> getAllStudents() {
        return studentsRepository.findAll().stream().map(studentsMapper::toDt).toList();
    }

    public StudentsViewDTO getStudentById(Long id) {
        return studentsMapper.toDt(studentsRepository.findById(id).orElseThrow());
    }

    @Transactional
    public StudentsViewDTO addStudent(StudentsWriteDTO student) {
        return save(student);
    }

    @Transactional
    public StudentsViewDTO updateStudent(StudentsWriteDTO student) {
        if (student.id() == null || !studentsRepository.existsById(student.id())) {
            throw new IllegalStateException("Id not found");
        }
        return save(student);
    }

    private StudentsViewDTO save(StudentsWriteDTO student) {
        return studentsMapper.toDt(studentsRepository.save(studentsMapper.toEntity(student)));
    }

    @Transactional
    public void deleteStudentById(Long id) {
        try {
            studentsRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalStateException("Id not found: " + id, e);
        }
    }
}
