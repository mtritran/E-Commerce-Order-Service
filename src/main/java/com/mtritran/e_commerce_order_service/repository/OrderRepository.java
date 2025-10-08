package com.mtritran.e_commerce_order_service.repository;

import com.mtritran.e_commerce_order_service.entity.Order;
import com.mtritran.e_commerce_order_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findByUserAndStatus(User user, String status);
}
