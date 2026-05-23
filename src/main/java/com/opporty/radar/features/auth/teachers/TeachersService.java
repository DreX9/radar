package com.opporty.radar.features.auth.teachers;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeachersService {
    private final TeachersRepository teachersRepository;
    private final TeachersMapper teachersMapper;

    public List<TeachersViewDTO> getAllTeachers() {
        return teachersRepository.findAll().stream().map(teachersMapper::toDt).toList();
    }
}
