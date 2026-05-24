package com.opporty.radar.common;

public record RegisterRequest(
        String username,
        String email,
        String password,
        Long roleId
) {
}
