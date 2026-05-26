package com.opporty.radar.features.auth.teachers;

import java.time.LocalDate;
import com.opporty.radar.features.auth.users.UsersViewDTO;

public record TeachersViewDTO(
        Long id,
        String nombres,
        String apellidos,
        String titulo,
        String especialidad,
        String telefono,
        String dni,
        LocalDate fechaNacimiento,
        String biography,
        String status,
        LocalDate hiringDate,
        UsersViewDTO user
) {}

