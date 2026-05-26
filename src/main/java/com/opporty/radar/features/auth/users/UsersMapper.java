package com.opporty.radar.features.auth.users;

import org.springframework.stereotype.Component;
import com.opporty.radar.common.MapperInterface;
import com.opporty.radar.features.auth.roles.Roles;
import com.opporty.radar.features.auth.roles.RolesMapper;
import com.opporty.radar.features.auth.students.StudentsRepository;
import com.opporty.radar.features.auth.teachers.TeachersRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsersMapper implements MapperInterface<Users, UsersWriteDTO, UsersViewDTO> {
    private final RolesMapper rolesMapper;
    private final StudentsRepository studentsRepository;
    private final TeachersRepository teachersRepository;

    @Override
    public UsersViewDTO toDt(Users entity) {
        if (entity == null) return null;

        String nombre = "Sin Nombre";
        String status = "ACTIVE";
        String career = null;
        String specialty = null;
        Integer ciclo = null;
        String dni = null;
        LocalDate fechaNacimiento = null;
        String phoneNumber = null;
        String biography = null;
        LocalDate hiringDate = null;
        String titulo = null;

        // Try to find student profile
        var studentOpt = studentsRepository.findByUser(entity);
        if (studentOpt.isPresent()) {
            var student = studentOpt.get();
            nombre = student.getNombres() + " " + student.getApellidos();
            status = student.getStatus();
            career = student.getCarrera();
            ciclo = student.getCiclo();
            dni = student.getDni();
            fechaNacimiento = student.getFechaNacimiento();
            phoneNumber = student.getPhoneNumber();
        } else {
            // Try to find teacher/manager/admin profile
            var teacherOpt = teachersRepository.findByUser(entity);
            if (teacherOpt.isPresent()) {
                var teacher = teacherOpt.get();
                nombre = teacher.getNombres() + " " + teacher.getApellidos();
                status = teacher.getStatus();
                specialty = teacher.getEspecialidad();
                dni = teacher.getDni();
                fechaNacimiento = teacher.getFechaNacimiento();
                phoneNumber = teacher.getTelefono();
                biography = teacher.getBiography();
                hiringDate = teacher.getHiringDate();
                titulo = teacher.getTitulo();
            }
        }

        return new UsersViewDTO(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.isEnabled(),
                rolesMapper.toDt(entity.getRole()),
                status,
                nombre,
                career,
                specialty,
                ciclo,
                dni,
                fechaNacimiento,
                phoneNumber,
                biography,
                hiringDate,
                titulo
        );
    }

    @Override
    public Users toEntity(UsersWriteDTO dto) {
        if (dto == null) return null;
        return Users.builder()
                .id(dto.id())
                .username(dto.username())
                .email(dto.email())
                .password(dto.password())
                .enabled(dto.enabled())
                .role(Roles.builder().id(dto.roleId()).build())
                .build();
    }
}
