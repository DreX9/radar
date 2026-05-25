package com.opporty.radar.features.auth.users;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UsersRestController {
    private final UsersService usersService;

    @GetMapping
    public ResponseEntity<List<UsersViewDTO>> list() throws ResponseStatusException {
        List<UsersViewDTO> list = usersService.getAllUsers();
        if (list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No data");
        }
        return ResponseEntity.ok(list);
    }
}
