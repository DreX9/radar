package com.opporty.radar.features.events.qr;

import jakarta.validation.constraints.NotBlank;

public record QrScanRequest(
    @NotBlank(message = "El token QR no puede estar vacío")
    String token
) {}
