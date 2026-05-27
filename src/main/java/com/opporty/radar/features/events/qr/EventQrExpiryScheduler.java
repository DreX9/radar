package com.opporty.radar.features.events.qr;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Scheduler that automatically marks expired QR sessions as inactive.
 * Runs every 60 seconds to deactivate any QR session whose expiresAt
 * has already passed, preventing them from being used even if the
 * lazy expiry check at scan time is bypassed.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EventQrExpiryScheduler {

    private final EventQrSessionsRepository eventQrSessionsRepository;

    @Scheduled(fixedRate = 60_000) // every 60 seconds
    @Transactional
    public void deactivateExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();
        List<EventQrSessions> expired = eventQrSessionsRepository.findByActiveTrueAndExpiresAtBefore(now);
        if (!expired.isEmpty()) {
            expired.forEach(session -> session.setActive(false));
            eventQrSessionsRepository.saveAll(expired);
            log.info("[QR Scheduler] Desactivadas {} sesión(es) QR expiradas.", expired.size());
        }
    }
}
