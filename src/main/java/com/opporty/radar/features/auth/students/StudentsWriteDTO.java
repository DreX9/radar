package com.opporty.radar.features.auth.students;

import java.time.LocalDate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record StudentsWriteDTO(
        Long id,
        @NotBlank @Size(max = 50) String codigo,
        @NotBlank @Size(max = 100) String nombres,
        @NotBlank @Size(max = 100) String apellidos,
        @Size(max = 100) String carrera,
        @Min(1) Integer ciclo,
        @NotBlank @Pattern(regexp = "\\d{8}") String dni,
        @NotNull LocalDate fechaNacimiento,
        @NotNull Long userId
) {}
