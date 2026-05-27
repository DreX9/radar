package com.opporty.radar.features.events.categories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCategoriesRepository extends JpaRepository<EventCategories, Long> {
    Optional<EventCategories> findByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
}
