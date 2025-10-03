package com.mtritran.e_commerce_order_service.service;

import com.mtritran.e_commerce_order_service.dto.request.RoleRequest;
import com.mtritran.e_commerce_order_service.dto.response.RoleResponse;
import com.mtritran.e_commerce_order_service.exception.AppException;
import com.mtritran.e_commerce_order_service.exception.ErrorCode;
import com.mtritran.e_commerce_order_service.mapper.RoleMapper;
import com.mtritran.e_commerce_order_service.repository.PermissionRepository;
import com.mtritran.e_commerce_order_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse createRole(RoleRequest request) {
        if (roleRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }

        var role = roleMapper.toRole(request);

        if (request.getPermissions() != null && !request.getPermissions().isEmpty()) {
            var permissions = permissionRepository.findByNameIn(request.getPermissions());
            role.setPermissions(new HashSet<>(permissions));
        }

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public RoleResponse getRole(String name) {
        var role = roleRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        return roleMapper.toRoleResponse(role);
    }

    public RoleResponse updateRole(String name, RoleRequest request) {
        var role = roleRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        if (request.getDescription() != null) {
            role.setDescription(request.getDescription());
        }

        if (request.getPermissions() != null && !request.getPermissions().isEmpty()) {
            var permissions = permissionRepository.findByNameIn(request.getPermissions());
            role.setPermissions(new HashSet<>(permissions));
        }

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public void deleteRole(String name) {
        var role = roleRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        roleRepository.delete(role);
    }
}