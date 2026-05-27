package com.opporty.radar.features.events.qr;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EventQrSessionsWriteDTO(
    @NotNull(message = "El ID del evento es requerido")
    Long eventId,

    @NotBlank(message = "El tipo de sesión QR es requerido")
    String type, // ENTRY, EXIT

    @Min(value = 1, message = "La duración mínima de validez es de 1 minuto")
    int durationMinutes
) {}
