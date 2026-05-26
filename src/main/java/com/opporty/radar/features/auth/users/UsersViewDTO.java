package com.opporty.radar.features.auth.users;

import com.opporty.radar.features.auth.roles.RolesViewDTO;
import java.time.LocalDate;

public record UsersViewDTO(
        Long id,
        String username,
        String email,
        boolean enabled,
        RolesViewDTO role,
        String status,
        String nombre,
        String career,
        String specialty,
        Integer ciclo,
        String dni,
        LocalDate fechaNacimiento,
        String phoneNumber,
        String biography,
        LocalDate hiringDate,
        String titulo
) {}

