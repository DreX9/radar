package com.opporty.radar.features.auth.students;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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
        return studentsRepository.findById(id)
                .map(studentsMapper::toDt)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado con ID: " + id));
    }

    @Transactional
    public StudentsViewDTO addStudent(StudentsWriteDTO student) {
        return save(student);
    }

    @Transactional
    public StudentsViewDTO updateStudent(StudentsWriteDTO dto) {
        if (dto.id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del estudiante es obligatorio para actualizar.");
        }
        Students existing = studentsRepository.findById(dto.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado con ID: " + dto.id()));

        if (existing.getUser() != null && !existing.getUser().getId().equals(dto.userId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se permite cambiar el usuario asociado.");
        }

        existing.setCodigo(dto.codigo());
        existing.setNombres(dto.nombres());
        existing.setApellidos(dto.apellidos());
        existing.setCarrera(dto.carrera());
        existing.setCiclo(dto.ciclo());
        existing.setDni(dto.dni());
        existing.setFechaNacimiento(dto.fechaNacimiento());

        return studentsMapper.toDt(studentsRepository.save(existing));
    }

    private StudentsViewDTO save(StudentsWriteDTO student) {
        return studentsMapper.toDt(studentsRepository.save(studentsMapper.toEntity(student)));
    }

    @Transactional
    public void deleteStudentById(Long id) {
        if (!studentsRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado con ID: " + id);
        }
        studentsRepository.deleteById(id);
    }
}
