package com.opporty.radar.features.events.core;

import java.time.LocalDateTime;

public record UpdateCheckDTO(
    Long eventsCount,
    LocalDateTime eventsLastUpdated,
    Long registrationsCount,
    LocalDateTime registrationsLastUpdated
) {}
