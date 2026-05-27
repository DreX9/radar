package com.opporty.radar.features.events.categories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record EventCategoriesWriteDTO(
    Long id,

    @NotBlank(message = "El nombre de la categoría no puede estar vacío")
    @Size(max = 100, message = "El nombre de la categoría no puede superar los 100 caracteres")
    String nombre,

    String descripcion,

    Set<Long> tagIds
) {}
