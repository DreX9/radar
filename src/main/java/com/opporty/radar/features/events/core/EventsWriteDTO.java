package com.opporty.radar.features.events.core;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public record EventsWriteDTO(
    Long id,

    @NotBlank(message = "El título del evento no puede estar vacío")
    @Size(max = 255, message = "El título no puede superar los 255 caracteres")
    String titulo,

    String descripcion,

    @NotNull(message = "La fecha de inicio es requerida")
    @FutureOrPresent(message = "La fecha de inicio debe ser en el presente o futuro")
    LocalDate fechaInicio,

    @NotNull(message = "La fecha de fin es requerida")
    LocalDate fechaFin,

    LocalTime horaInicio,
    LocalTime horaFin,

    @Min(value = 1, message = "La capacidad debe ser de al menos 1 persona")
    Integer capacidad,

    @Size(max = 500, message = "La URL de la imagen principal no puede superar los 500 caracteres")
    String imagenUrl,

    @NotBlank(message = "La modalidad del evento es requerida")
    String modalidad, // ENUM as String: PRESENCIAL, VIRTUAL, HIBRIDO

    @Size(max = 255, message = "El lugar no puede superar los 255 caracteres")
    String lugar,

    @Size(max = 255, message = "La referencia no puede superar los 255 caracteres")
    String referencia,

    @DecimalMin(value = "-90.0", message = "La latitud mínima permitida es -90")
    @DecimalMax(value = "90.0", message = "La latitud máxima permitida es 90")
    BigDecimal latitud,

    @DecimalMin(value = "-180.0", message = "La longitud mínima permitida es -180")
    @DecimalMax(value = "180.0", message = "La longitud máxima permitida es 180")
    BigDecimal longitud,

    @NotBlank(message = "El estado del evento es requerido")
    String estado, // ENUM as String: DRAFT, PUBLISHED, CANCELLED, FINISHED

    boolean requiresApproval,
    boolean allowQrAttendance,
    Integer edadMinima,
    String requisitos,

    @NotEmpty(message = "Se debe seleccionar al menos una categoría para el evento")
    Set<Long> categoryIds,

    Set<Long> tagIds,

    List<String> imageUrls // List of additional image URLs
) {}

