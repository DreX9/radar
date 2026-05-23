package com.opporty.radar.features.users;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository userRepository;
    private final UsersMapper userMapper;

    public List<UsersViewDTO> getAllUsers() {

        return userRepository.findAll().stream().map(userMapper::toDt).toList();
    }
}
