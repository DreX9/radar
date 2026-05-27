package com.opporty.radar.features.events.tags;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TagsWriteDTO(
    Long id,

    @NotBlank(message = "El nombre del tag no puede estar vacío")
    @Size(max = 100, message = "El nombre del tag no puede superar los 100 caracteres")
    String nombre
) {}
