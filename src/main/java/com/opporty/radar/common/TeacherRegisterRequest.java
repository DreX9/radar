package com.opporty.radar.common;

import java.time.LocalDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TeacherRegisterRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6) String password,
        @NotBlank @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos") String dni,
        @NotNull LocalDate birthDate,
        @NotBlank @Size(max = 100) String firstName,
        @NotBlank @Size(max = 100) String lastName,
        @Size(max = 150) String title,
        @Size(max = 150) String specialty,
        @Pattern(regexp = "\\d{9}", message = "El número de teléfono debe tener exactamente 9 dígitos") String phoneNumber,
        String biography,
        @NotBlank String status,
        LocalDate hiringDate,
        @NotNull Long roleId
) {
}

