package com.opporty.radar.features.events.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventImagesRepository extends JpaRepository<EventImages, Long> {
}
