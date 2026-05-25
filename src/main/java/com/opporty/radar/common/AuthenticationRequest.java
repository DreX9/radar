package com.opporty.radar.common;

public record AuthenticationRequest(
        String username,
        String password
) {
}
