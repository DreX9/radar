package com.opporty.radar.features.auth.users;

import com.opporty.radar.features.auth.roles.RolesViewDTO;

public record UsersViewDTO(
        Long id,
        String username,
        String email,
        boolean enabled,
        RolesViewDTO role
) {}
