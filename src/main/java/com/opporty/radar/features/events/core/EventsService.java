package com.opporty.radar.features.events.core;

import com.opporty.radar.features.auth.users.Users;
import com.opporty.radar.features.events.categories.EventCategories;
import com.opporty.radar.features.events.categories.EventCategoriesRepository;
import com.opporty.radar.features.events.tags.Tags;
import com.opporty.radar.features.events.tags.TagsRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class EventsService {

    private final EventsRepository eventsRepository;
    private final EventCategoriesRepository eventCategoriesRepository;
    private final TagsRepository tagsRepository;
    private final EventsMapper eventsMapper;

    @Transactional(readOnly = true)
    public List<EventsViewDTO> getAllEvents() {
        return eventsRepository.findAll()
                .stream()
                .map(eventsMapper::toDt)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventsViewDTO getEventById(Long id) {
        Events event = eventsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado con ID: " + id));
        return eventsMapper.toDt(event);
    }

    @Transactional
    public EventsViewDTO createEvent(EventsWriteDTO dto, Users createdBy) {
        if (dto.fechaFin().isBefore(dto.fechaInicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        // Resolve multiple categories
        Set<EventCategories> categories = new HashSet<>(eventCategoriesRepository.findAllById(dto.categoryIds()));
        if (categories.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontraron las categorías con los IDs proporcionados.");
        }

        Events event = eventsMapper.toEntity(dto);
        event.setCategories(categories);
        event.setCreatedBy(createdBy);

        // Resolve tags
        if (dto.tagIds() != null && !dto.tagIds().isEmpty()) {
            Set<Tags> tags = new HashSet<>(tagsRepository.findAllById(dto.tagIds()));
            event.setTags(tags);
        }

        // Resolve images: first image also populates imagenUrl as the main image
        if (dto.imageUrls() != null && !dto.imageUrls().isEmpty()) {
            if (event.getImagenUrl() == null || event.getImagenUrl().isBlank()) {
                event.setImagenUrl(dto.imageUrls().get(0));
            }
            List<EventImages> images = dto.imageUrls().stream()
                    .map(url -> EventImages.builder().event(event).imageUrl(url).build())
                    .collect(Collectors.toList());
            event.setImages(images);
        }

        Events savedEvent = eventsRepository.save(event);
        return eventsMapper.toDt(savedEvent);
    }

    @Transactional
    public EventsViewDTO updateEvent(Long id, EventsWriteDTO dto) {
        Events event = eventsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado con ID: " + id));

        if (dto.fechaFin().isBefore(dto.fechaInicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        // Parse enums
        Modalidad modality;
        try {
            modality = Modalidad.valueOf(dto.modalidad().toUpperCase());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Modalidad inválida: " + dto.modalidad());
        }

        Estado state;
        try {
            state = Estado.valueOf(dto.estado().toUpperCase());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado inválido: " + dto.estado());
        }

        // Update scalar fields
        event.setTitulo(dto.titulo());
        event.setDescripcion(dto.descripcion());
        event.setFechaInicio(dto.fechaInicio());
        event.setFechaFin(dto.fechaFin());
        event.setHoraInicio(dto.horaInicio());
        event.setHoraFin(dto.horaFin());
        event.setCapacidad(dto.capacidad());
        event.setImagenUrl(dto.imagenUrl());
        event.setModalidad(modality);
        event.setLugar(dto.lugar());
        event.setReferencia(dto.referencia());
        event.setLatitud(dto.latitud());
        event.setLongitud(dto.longitud());
        event.setEstado(state);
        event.setRequiresApproval(dto.requiresApproval());
        event.setAllowQrAttendance(dto.allowQrAttendance());
        event.setEdadMinima(dto.edadMinima());
        event.setRequisitos(dto.requisitos());

        // Update categories
        if (dto.categoryIds() != null && !dto.categoryIds().isEmpty()) {
            Set<EventCategories> categories = new HashSet<>(eventCategoriesRepository.findAllById(dto.categoryIds()));
            event.setCategories(categories);
        } else {
            event.getCategories().clear();
        }

        // Update tags
        if (dto.tagIds() != null) {
            Set<Tags> tags = new HashSet<>(tagsRepository.findAllById(dto.tagIds()));
            event.setTags(tags);
        } else {
            event.getTags().clear();
        }

        // Update images (clear old ones and add new ones)
        event.getImages().clear();
        if (dto.imageUrls() != null && !dto.imageUrls().isEmpty()) {
            // First image becomes the main image if not explicitly set
            if (event.getImagenUrl() == null || event.getImagenUrl().isBlank()) {
                event.setImagenUrl(dto.imageUrls().get(0));
            }
            List<EventImages> images = dto.imageUrls().stream()
                    .map(url -> EventImages.builder().event(event).imageUrl(url).build())
                    .collect(Collectors.toList());
            event.getImages().addAll(images);
        }

        Events updatedEvent = eventsRepository.save(event);
        return eventsMapper.toDt(updatedEvent);
    }

    @Transactional
    public void deleteEventById(Long id) {
        if (!eventsRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado con ID: " + id);
        }
        eventsRepository.deleteById(id);
    }
}
