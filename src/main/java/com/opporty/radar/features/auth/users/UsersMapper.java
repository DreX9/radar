package com.opporty.radar.features.auth.users;

import org.springframework.stereotype.Component;
import com.opporty.radar.common.MapperInterface;
import com.opporty.radar.features.auth.roles.Roles;
import com.opporty.radar.features.auth.roles.RolesMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsersMapper implements MapperInterface<Users, UsersWriteDTO, UsersViewDTO> {
    private final RolesMapper rolesMapper;

    @Override
    public UsersViewDTO toDt(Users entity) {
        if (entity == null) return null;
        return new UsersViewDTO(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.isEnabled(),
                rolesMapper.toDt(entity.getRole())
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
