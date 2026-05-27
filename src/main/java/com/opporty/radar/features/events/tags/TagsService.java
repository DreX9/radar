package com.opporty.radar.features.events.tags;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TagsService {

    private final TagsRepository tagsRepository;
    private final TagsMapper tagsMapper;

    @Transactional(readOnly = true)
    public List<TagsViewDTO> getAllTags() {
        return tagsRepository.findAll()
                .stream()
                .map(tagsMapper::toDt)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TagsViewDTO getTagById(Long id) {
        Tags tag = tagsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag no encontrado con ID: " + id));
        return tagsMapper.toDt(tag);
    }

    @Transactional
    public TagsViewDTO addTag(TagsWriteDTO dto) {
        if (tagsRepository.existsByNombreIgnoreCase(dto.nombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tag con el nombre '" + dto.nombre() + "' ya existe.");
        }
        Tags tag = tagsMapper.toEntity(dto);
        Tags savedTag = tagsRepository.save(tag);
        return tagsMapper.toDt(savedTag);
    }

    @Transactional
    public void deleteTagById(Long id) {
        if (!tagsRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag no encontrado con ID: " + id);
        }
        tagsRepository.deleteById(id);
    }
}
