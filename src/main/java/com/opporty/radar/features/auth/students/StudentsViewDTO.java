package com.opporty.radar.features.auth.students;

import java.time.LocalDate;
import com.opporty.radar.features.auth.users.UsersViewDTO;

public record StudentsViewDTO(
        Long id,
        String nombres,
        String apellidos,
        String carrera,
        Integer ciclo,
        String dni,
        LocalDate fechaNacimiento,
        String phoneNumber,
        String status,
        UsersViewDTO user
) {}

