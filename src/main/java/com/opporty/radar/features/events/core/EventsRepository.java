package com.opporty.radar.features.events.core;

import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepository extends JpaRepository<Events, Long> {
    List<Events> findByEstado(Estado estado);

    List<Events> findAllByOrderByCreatedAtDesc();

    @Query("SELECT COUNT(e) FROM Events e WHERE e.estado = com.opporty.radar.features.events.core.Estado.PUBLISHED")
    long countPublishedEvents();

    @Query("SELECT MAX(COALESCE(e.updatedAt, e.createdAt)) FROM Events e WHERE e.estado = com.opporty.radar.features.events.core.Estado.PUBLISHED")
    LocalDateTime getMaxPublishedEventUpdateTime();

    @Query("SELECT COUNT(er) FROM EventRegistrations er WHERE er.event.estado = com.opporty.radar.features.events.core.Estado.PUBLISHED")
    long countRegistrationsForPublishedEvents();

    @Query("SELECT MAX(COALESCE(er.updatedAt, er.createdAt)) FROM EventRegistrations er WHERE er.event.estado = com.opporty.radar.features.events.core.Estado.PUBLISHED")
    LocalDateTime getMaxRegistrationTimeForPublishedEvents();

    /**
     * Obtiene el evento con bloqueo pesimista (SELECT FOR UPDATE).
     * Úsalo al verificar + modificar aforo para evitar race conditions
     * entre registros manuales y escaneos QR concurrentes.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM Events e WHERE e.id = :id")
    Optional<Events> findByIdWithLock(@Param("id") Long id);
}
