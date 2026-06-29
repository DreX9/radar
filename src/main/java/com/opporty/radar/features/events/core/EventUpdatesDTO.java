package com.opporty.radar.features.events.core;

import java.time.LocalDateTime;

public record EventUpdatesDTO(
    long eventsCount,
    LocalDateTime eventsLastUpdated,
    long registrationsCount,
    LocalDateTime registrationsLastUpdated
) {}
