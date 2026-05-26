package com.opporty.radar.features.auth.teachers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeachersService {
    private final TeachersRepository teachersRepository;
    private final TeachersMapper teachersMapper;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;


    public List<TeachersViewDTO> getAllTeachers() {
        return teachersRepository.findAll().stream().map(teachersMapper::toDt).toList();
    }

    public TeachersViewDTO getTeacherById(Long id) {
        return teachersRepository.findById(id)
                .map(teachersMapper::toDt)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado con ID: " + id));
    }

    public TeachersViewDTO getTeacherByUsername(String username) {
        return teachersRepository.findByUserUsername(username)
                .map(teachersMapper::toDt)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado para el usuario: " + username));
    }


    @Transactional
    public TeachersViewDTO addTeacher(TeachersWriteDTO teacher) {
        return save(teacher);
    }

    @Transactional
    public TeachersViewDTO updateTeacher(TeachersWriteDTO dto) {
        if (dto.id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del profesor es obligatorio para actualizar.");
        }
        Teachers existing = teachersRepository.findById(dto.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado con ID: " + dto.id()));

        if (existing.getUser() != null && !existing.getUser().getId().equals(dto.userId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se permite cambiar el usuario asociado.");
        }

        existing.setNombres(dto.nombres());
        existing.setApellidos(dto.apellidos());
        existing.setTitulo(dto.titulo());
        existing.setEspecialidad(dto.especialidad());
        existing.setTelefono(dto.telefono());
        existing.setDni(dto.dni());
        existing.setFechaNacimiento(dto.fechaNacimiento());
        existing.setBiography(dto.biography());
        existing.setStatus(dto.status());
        existing.setHiringDate(dto.hiringDate());

        if (dto.password() != null && !dto.password().trim().isEmpty()) {
            existing.getUser().setPassword(passwordEncoder.encode(dto.password().trim()));
        }

        return teachersMapper.toDt(teachersRepository.save(existing));
    }


    private TeachersViewDTO save(TeachersWriteDTO teacher) {
        return teachersMapper.toDt(teachersRepository.save(teachersMapper.toEntity(teacher)));
    }

    @Transactional
    public void deleteTeacherById(Long id) {
        if (!teachersRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesor no encontrado con ID: " + id);
        }
        teachersRepository.deleteById(id);
    }
}
