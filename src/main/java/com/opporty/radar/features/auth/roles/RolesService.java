package com.opporty.radar.features.auth.roles;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RolesService {
    private final RolesRepository rolesRepository;
    private final RolesMapper rolesMapper;

    public List<RolesViewDTO> getAllRoles() {
        return rolesRepository.findAll().stream().map(rolesMapper::toDt).toList();
    }
}
