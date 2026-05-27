package com.opporty.radar.features.events.categories;

import com.opporty.radar.features.events.tags.TagsViewDTO;
import java.time.LocalDateTime;
import java.util.Set;

public record EventCategoriesViewDTO(
    Long id,
    String nombre,
    String descripcion,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Set<TagsViewDTO> tags
) {}
