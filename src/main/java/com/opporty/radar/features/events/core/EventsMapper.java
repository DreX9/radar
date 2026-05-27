package com.opporty.radar.features.events.core;

import com.opporty.radar.common.MapperInterface;
import com.opporty.radar.features.events.categories.EventCategoriesMapper;
import com.opporty.radar.features.events.categories.EventCategoriesViewDTO;
import com.opporty.radar.features.events.tags.TagsMapper;
import com.opporty.radar.features.events.tags.TagsViewDTO;
import java.util.Collections;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventsMapper implements MapperInterface<Events, EventsWriteDTO, EventsViewDTO> {

    private final TagsMapper tagsMapper;
    private final EventCategoriesMapper eventCategoriesMapper;

    @Override
    public EventsViewDTO toDt(Events entity) {
        if (entity == null) {
            return null;
        }

        Set<EventCategoriesViewDTO> mappedCategories = entity.getCategories() != null
                ? entity.getCategories().stream()
                        .map(eventCategoriesMapper::toDt)
                        .collect(Collectors.toSet())
                : Collections.emptySet();

        Set<TagsViewDTO> mappedTags = entity.getTags() != null
                ? entity.getTags().stream()
                        .map(tagsMapper::toDt)
                        .collect(Collectors.toSet())
                : Collections.emptySet();

        List<String> imagesList = entity.getImages() != null
                ? entity.getImages().stream()
                        .map(EventImages::getImageUrl)
                        .collect(Collectors.toList())
                : Collections.emptyList();

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
                entity.getEdadMinima(),
                entity.getRequisitos(),
                entity.getMotivoRechazo(),
                entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null,
                entity.getCreatedBy() != null ? entity.getCreatedBy().getUsername() : null,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                mappedCategories,
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
                .edadMinima(dto.edadMinima())
                .requisitos(dto.requisitos())
                .motivoRechazo(dto.motivoRechazo())
                .build();
    }
}
