package com.opporty.radar.common;

import java.time.LocalDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record StudentRegisterRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6) String password,
        @NotBlank @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos") String dni,
        @NotNull LocalDate fechaNacimiento,
        @NotBlank @Size(max = 100) String nombres,
        @NotBlank @Size(max = 100) String apellidos,
        @Size(max = 100) String carrera,
        @Min(1) Integer ciclo
) {
}
