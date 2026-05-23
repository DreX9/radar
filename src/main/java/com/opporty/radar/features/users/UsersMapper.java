package com.opporty.radar.features.users;

import org.springframework.stereotype.Component;

import com.opporty.radar.common.MapperInterface;

@Component
public class UsersMapper implements MapperInterface<Users, UsersWriteDTO, UsersViewDTO> {

    @Override
    public UsersViewDTO toDt(Users entity) {
        return new UsersViewDTO(
                entity.getId(),
                entity.getName());
    }

    @Override
    public Users toEntity(UsersWriteDTO dto) {
        return Users.builder()
                .id(dto.id())
                .name(dto.name())
                .build();
    }

}
