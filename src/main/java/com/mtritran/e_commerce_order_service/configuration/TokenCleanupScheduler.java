package com.mtritran.e_commerce_order_service.configuration;

import com.mtritran.e_commerce_order_service.repository.InvalidatedTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {

    private final InvalidatedTokenRepository invalidatedTokenRepository;

    // chạy mỗi ngày lúc 0h
    @Scheduled(cron = "0 0 0 * * *")
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        int deletedCount = invalidatedTokenRepository.deleteByExpiryTimeBefore(now);
        log.info("✅ Dọn rác token: {} token hết hạn đã bị xóa", deletedCount);
    }
}
