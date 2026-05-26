package com.opporty.radar.features.auth.teachers;

import org.springframework.stereotype.Component;
import com.opporty.radar.common.MapperInterface;
import com.opporty.radar.features.auth.users.Users;
import com.opporty.radar.features.auth.users.UsersMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TeachersMapper implements MapperInterface<Teachers, TeachersWriteDTO, TeachersViewDTO> {
    private final UsersMapper usersMapper;

    @Override
    public TeachersViewDTO toDt(Teachers entity) {
        if (entity == null) return null;
        return new TeachersViewDTO(
                entity.getId(),
                entity.getNombres(),
                entity.getApellidos(),
                entity.getTitulo(),
                entity.getEspecialidad(),
                entity.getTelefono(),
                entity.getDni(),
                entity.getFechaNacimiento(),
                entity.getBiography(),
                entity.getStatus(),
                entity.getHiringDate(),
                usersMapper.toDt(entity.getUser())
        );
    }

    @Override
    public Teachers toEntity(TeachersWriteDTO dto) {
        if (dto == null) return null;
        return Teachers.builder()
                .id(dto.id())
                .nombres(dto.nombres())
                .apellidos(dto.apellidos())
                .titulo(dto.titulo())
                .especialidad(dto.especialidad())
                .telefono(dto.telefono())
                .dni(dto.dni())
                .fechaNacimiento(dto.fechaNacimiento())
                .biography(dto.biography())
                .status(dto.status())
                .hiringDate(dto.hiringDate())
                .user(Users.builder().id(dto.userId()).build())
                .build();
    }
}

