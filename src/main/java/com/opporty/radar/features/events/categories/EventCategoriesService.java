package com.opporty.radar.features.events.categories;

import com.opporty.radar.features.events.tags.Tags;
import com.opporty.radar.features.events.tags.TagsRepository;
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
public class EventCategoriesService {

    private final EventCategoriesRepository eventCategoriesRepository;
    private final TagsRepository tagsRepository;
    private final EventCategoriesMapper eventCategoriesMapper;

    @Transactional(readOnly = true)
    public List<EventCategoriesViewDTO> getAllCategories() {
        return eventCategoriesRepository.findAll()
                .stream()
                .map(eventCategoriesMapper::toDt)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventCategoriesViewDTO getCategoryById(Long id) {
        EventCategories category = eventCategoriesRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada con ID: " + id));
        return eventCategoriesMapper.toDt(category);
    }

    @Transactional
    public EventCategoriesViewDTO createCategory(EventCategoriesWriteDTO dto) {
        if (eventCategoriesRepository.existsByNombreIgnoreCase(dto.nombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe una categoría con el nombre '" + dto.nombre() + "'.");
        }

        EventCategories category = eventCategoriesMapper.toEntity(dto);

        if (dto.tagIds() != null && !dto.tagIds().isEmpty()) {
            Set<Tags> tags = new HashSet<>(tagsRepository.findAllById(dto.tagIds()));
            category.setTags(tags);
        }

        EventCategories saved = eventCategoriesRepository.save(category);
        return eventCategoriesMapper.toDt(saved);
    }

    @Transactional
    public EventCategoriesViewDTO updateCategory(Long id, EventCategoriesWriteDTO dto) {
        EventCategories category = eventCategoriesRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada con ID: " + id));

        // Check if name is being changed to another existing name
        if (!category.getNombre().equalsIgnoreCase(dto.nombre()) && eventCategoriesRepository.existsByNombreIgnoreCase(dto.nombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe otra categoría con el nombre '" + dto.nombre() + "'.");
        }

        category.setNombre(dto.nombre());
        category.setDescripcion(dto.descripcion());

        if (dto.tagIds() != null) {
            Set<Tags> tags = new HashSet<>(tagsRepository.findAllById(dto.tagIds()));
            category.setTags(tags);
        } else {
            category.getTags().clear();
        }

        EventCategories updated = eventCategoriesRepository.save(category);
        return eventCategoriesMapper.toDt(updated);
    }

    @Transactional
    public void deleteCategoryById(Long id) {
        if (!eventCategoriesRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada con ID: " + id);
        }
        eventCategoriesRepository.deleteById(id);
    }
}
