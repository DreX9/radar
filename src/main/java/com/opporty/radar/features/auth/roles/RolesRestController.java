package com.opporty.radar.features.auth.roles;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("roles")
@RequiredArgsConstructor
public class RolesRestController {
    private final RolesService rolesService;

    @GetMapping
    public ResponseEntity<List<RolesViewDTO>> list() throws ResponseStatusException {
        List<RolesViewDTO> list = rolesService.getAllRoles();
        if (list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No data");
        }
        return ResponseEntity.ok(list);
    }
}
