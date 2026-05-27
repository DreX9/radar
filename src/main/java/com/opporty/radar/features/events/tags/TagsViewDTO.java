package com.opporty.radar.features.events.tags;

import java.time.LocalDateTime;

public record TagsViewDTO(
    Long id,
    String nombre,
    LocalDateTime createdAt
) {}
