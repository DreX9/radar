package com.opporty.radar.features.events.core;

import com.opporty.radar.common.MapperInterface;
import com.opporty.radar.features.events.categories.EventCategories;
import com.opporty.radar.features.events.tags.TagsMapper;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventsMapper implements MapperInterface<Events, EventsWriteDTO, EventsViewDTO> {

    private final TagsMapper tagsMapper;

    @Override
    public EventsViewDTO toDt(Events entity) {
        if (entity == null) {
            return null;
        }

        var mappedTags = entity.getTags() != null ? entity.getTags().stream()
                .map(tagsMapper::toDt)
                .collect(Collectors.toSet()) : Collections.<com.opporty.radar.features.events.tags.TagsViewDTO>emptySet();

        var imagesList = entity.getImages() != null ? entity.getImages().stream()
                .map(EventImages::getImageUrl)
                .collect(Collectors.toList()) : Collections.<String>emptyList();

        return new EventsViewDTO(
                entity.getId(),
                entity.getTitulo(),
                entity.getDescripcion(),
                entity.getFechaInicio(),
                entity.getFechaFin(),
                entity.getHoraInicio(),
                entity.getHoraFin(),
                entity.getCapacidad(),
                entity.getImagenUrl(),
                entity.getModalidad().name(),
                entity.getLugar(),
                entity.getReferencia(),
                entity.getLatitud(),
                entity.getLongitud(),
                entity.getEstado().name(),
                entity.isRequiresApproval(),
                entity.isAllowQrAttendance(),
                entity.getCategory() != null ? entity.getCategory().getId() : null,
                entity.getCategory() != null ? entity.getCategory().getNombre() : null,
                entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null,
                entity.getCreatedBy() != null ? entity.getCreatedBy().getUsername() : null,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                mappedTags,
                imagesList
        );
    }

    @Override
    public Events toEntity(EventsWriteDTO dto) {
        if (dto == null) {
            return null;
        }

        Modalidad modality;
        try {
            modality = Modalidad.valueOf(dto.modalidad().toUpperCase());
        } catch (Exception e) {
            modality = Modalidad.PRESENCIAL;
        }

        Estado state;
        try {
            state = Estado.valueOf(dto.estado().toUpperCase());
        } catch (Exception e) {
            state = Estado.DRAFT;
        }

        return Events.builder()
                .id(dto.id())
                .titulo(dto.titulo())
                .descripcion(dto.descripcion())
                .fechaInicio(dto.fechaInicio())
                .fechaFin(dto.fechaFin())
                .horaInicio(dto.horaInicio())
                .horaFin(dto.horaFin())
                .capacidad(dto.capacidad())
                .imagenUrl(dto.imagenUrl())
                .modalidad(modality)
                .lugar(dto.lugar())
                .referencia(dto.referencia())
                .latitud(dto.latitud())
                .longitud(dto.longitud())
                .estado(state)
                .requiresApproval(dto.requiresApproval())
                .allowQrAttendance(dto.allowQrAttendance())
                .category(EventCategories.builder().id(dto.categoryId()).build())
                .build();
    }
}
