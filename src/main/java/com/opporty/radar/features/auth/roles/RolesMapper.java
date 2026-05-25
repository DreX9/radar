package com.opporty.radar.features.auth.roles;

import org.springframework.stereotype.Component;
import com.opporty.radar.common.MapperInterface;

@Component
public class RolesMapper implements MapperInterface<Roles, RolesWriteDTO, RolesViewDTO> {

    @Override
    public RolesViewDTO toDt(Roles entity) {
        if (entity == null) return null;
        return new RolesViewDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription());
    }

    @Override
    public Roles toEntity(RolesWriteDTO dto) {
        if (dto == null) return null;
        return Roles.builder()
                .id(dto.id())
                .name(dto.name())
                .description(dto.description())
                .build();
    }
}
