package com.mtritran.e_commerce_order_service.repository;

import com.mtritran.e_commerce_order_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(String name);
    void deleteByName(String name);
    boolean existsByName(String name);
    List<Role> findByNameIn(Set<String> names);
}
