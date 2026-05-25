package com.opporty.radar.features.auth.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsersWriteDTO(
        Long id,
        @NotBlank @Size(max = 100) String username,
        @NotBlank @Email String email,
        @NotBlank String password,
        boolean enabled,
        @NotNull Long roleId
) {}
