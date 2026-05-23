package com.opporty.radar.features.auth.teachers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TeachersWriteDTO(
        Long id,
        @NotBlank @Size(max = 100) String nombres,
        @NotBlank @Size(max = 100) String apellidos,
        @Size(max = 150) String titulo,
        @Size(max = 150) String especialidad,
        @Size(max = 20) String telefono,
        @NotNull Long userId
) {}
