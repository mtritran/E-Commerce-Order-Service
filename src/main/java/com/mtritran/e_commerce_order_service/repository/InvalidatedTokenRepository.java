package com.mtritran.e_commerce_order_service.repository;

import com.mtritran.e_commerce_order_service.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
    int deleteByExpiryTimeBefore(LocalDateTime time);
}
