package com.opporty.radar.features.auth.roles;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RolesWriteDTO(
        Long id,
        @NotBlank @Size(max = 100) String name,
        @Size(max = 255) String description
) {}
