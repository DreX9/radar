package com.opporty.radar.features.auth.teachers;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeachersService {
    private final TeachersRepository teachersRepository;
    private final TeachersMapper teachersMapper;

    public List<TeachersViewDTO> getAllTeachers() {
        return teachersRepository.findAll().stream().map(teachersMapper::toDt).toList();
    }

    public TeachersViewDTO getTeacherById(Long id) {
        return teachersMapper.toDt(teachersRepository.findById(id).orElseThrow());
    }

    @Transactional
    public TeachersViewDTO addTeacher(TeachersWriteDTO teacher) {
        return save(teacher);
    }

    @Transactional
    public TeachersViewDTO updateTeacher(TeachersWriteDTO teacher) {
        if (teacher.id() == null || !teachersRepository.existsById(teacher.id())) {
            throw new IllegalStateException("Id not found");
        }
        return save(teacher);
    }

    private TeachersViewDTO save(TeachersWriteDTO teacher) {
        return teachersMapper.toDt(teachersRepository.save(teachersMapper.toEntity(teacher)));
    }

    @Transactional
    public void deleteTeacherById(Long id) {
        try {
            teachersRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalStateException("Id not found: " + id, e);
        }
    }
}
