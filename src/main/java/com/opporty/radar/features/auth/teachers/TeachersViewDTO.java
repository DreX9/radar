package com.opporty.radar.features.auth.teachers;

import com.opporty.radar.features.auth.users.UsersViewDTO;

public record TeachersViewDTO(
        Long id,
        String nombres,
        String apellidos,
        String titulo,
        String especialidad,
        String telefono,
        UsersViewDTO user
) {}
