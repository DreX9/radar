package com.opporty.radar.features.events.categories;

import com.opporty.radar.common.MapperInterface;
import com.opporty.radar.features.events.tags.TagsMapper;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventCategoriesMapper implements MapperInterface<EventCategories, EventCategoriesWriteDTO, EventCategoriesViewDTO> {

    private final TagsMapper tagsMapper;

    @Override
    public EventCategoriesViewDTO toDt(EventCategories entity) {
        if (entity == null) {
            return null;
        }
        var mappedTags = entity.getTags() != null ? entity.getTags().stream()
                .map(tagsMapper::toDt)
                .collect(Collectors.toSet()) : java.util.Collections.<com.opporty.radar.features.events.tags.TagsViewDTO>emptySet();

        return new EventCategoriesViewDTO(
                entity.getId(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                mappedTags
        );
    }

    @Override
    public EventCategories toEntity(EventCategoriesWriteDTO dto) {
        if (dto == null) {
            return null;
        }
        return EventCategories.builder()
                .id(dto.id())
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .build();
    }
}
