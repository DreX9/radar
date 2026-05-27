package com.opporty.radar.features.events.registrations;

import jakarta.validation.constraints.NotNull;

public record EventRegistrationsWriteDTO(
    @NotNull(message = "El ID del evento es requerido")
    Long eventId,

    String roleInEvent, // ATTENDEE, SPEAKER, ORGANIZER. Defaults to ATTENDEE.

    String notes
) {}
