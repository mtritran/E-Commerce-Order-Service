package com.mtritran.e_commerce_order_service.repository;

import com.mtritran.e_commerce_order_service.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
}
