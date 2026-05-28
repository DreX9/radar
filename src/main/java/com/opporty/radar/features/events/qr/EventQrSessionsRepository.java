package com.opporty.radar.features.events.qr;

import com.opporty.radar.features.events.core.Events;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventQrSessionsRepository extends JpaRepository<EventQrSessions, Long> {
    Optional<EventQrSessions> findByTokenAndActiveTrue(String token);
    List<EventQrSessions> findByEventAndTypeAndActiveTrue(Events event, QrSessionType type);
    List<EventQrSessions> findByActiveTrueAndExpiresAtBefore(LocalDateTime now);
    boolean existsByEventAndType(Events event, QrSessionType type);
}

