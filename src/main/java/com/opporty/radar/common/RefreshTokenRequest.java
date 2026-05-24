package com.opporty.radar.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RefreshTokenRequest(
        @JsonProperty("refresh_token") String refreshToken
) {
}
