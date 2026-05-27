package com.opporty.radar.features.events.tags;

import com.opporty.radar.common.MapperInterface;
import org.springframework.stereotype.Component;

@Component
public class TagsMapper implements MapperInterface<Tags, TagsWriteDTO, TagsViewDTO> {

    @Override
    public TagsViewDTO toDt(Tags entity) {
        if (entity == null) {
            return null;
        }
        return new TagsViewDTO(
                entity.getId(),
                entity.getNombre(),
                entity.getCreatedAt()
        );
    }

    @Override
    public Tags toEntity(TagsWriteDTO dto) {
        if (dto == null) {
            return null;
        }
        return Tags.builder()
                .id(dto.id())
                .nombre(dto.nombre())
                .build();
    }
}
