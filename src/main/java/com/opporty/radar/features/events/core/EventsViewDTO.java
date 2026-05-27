package com.opporty.radar.features.events.core;

import com.opporty.radar.features.events.categories.EventCategoriesViewDTO;
import com.opporty.radar.features.events.tags.TagsViewDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public record EventsViewDTO(
    Long id,
    String titulo,
    String descripcion,
    LocalDate fechaInicio,
    LocalDate fechaFin,
    LocalTime horaInicio,
    LocalTime horaFin,
    Integer capacidad,
    String imagenUrl,
    String modalidad,
    String lugar,
    String referencia,
    BigDecimal latitud,
    BigDecimal longitud,
    String estado,
    boolean requiresApproval,
    boolean allowQrAttendance,
    Integer edadMinima,
    String requisitos,
    Long createdById,
    String createdByUsername,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Set<EventCategoriesViewDTO> categories,
    Set<TagsViewDTO> tags,
    List<String> imageUrls
) {}

