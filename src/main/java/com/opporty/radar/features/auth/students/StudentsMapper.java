package com.opporty.radar.features.auth.students;

import org.springframework.stereotype.Component;
import com.opporty.radar.common.MapperInterface;
import com.opporty.radar.features.auth.users.Users;
import com.opporty.radar.features.auth.users.UsersMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StudentsMapper implements MapperInterface<Students, StudentsWriteDTO, StudentsViewDTO> {
    private final UsersMapper usersMapper;

    @Override
    public StudentsViewDTO toDt(Students entity) {
        if (entity == null) return null;
        return new StudentsViewDTO(
                entity.getId(),
                entity.getCodigo(),
                entity.getNombres(),
                entity.getApellidos(),
                entity.getCarrera(),
                entity.getCiclo(),
                usersMapper.toDt(entity.getUser())
        );
    }

    @Override
    public Students toEntity(StudentsWriteDTO dto) {
        if (dto == null) return null;
        return Students.builder()
                .id(dto.id())
                .codigo(dto.codigo())
                .nombres(dto.nombres())
                .apellidos(dto.apellidos())
                .carrera(dto.carrera())
                .ciclo(dto.ciclo())
                .user(Users.builder().id(dto.userId()).build())
                .build();
    }
}
