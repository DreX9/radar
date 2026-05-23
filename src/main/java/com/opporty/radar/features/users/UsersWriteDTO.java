package com.opporty.radar.features.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsersWriteDTO(
        Long id,
        @NotBlank @Size(max = 100) String name) {

}
