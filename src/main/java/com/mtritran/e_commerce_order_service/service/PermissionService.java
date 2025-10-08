package com.mtritran.e_commerce_order_service.service;

import com.mtritran.e_commerce_order_service.dto.request.PermissionRequest;
import com.mtritran.e_commerce_order_service.dto.response.PermissionResponse;
import com.mtritran.e_commerce_order_service.entity.Permission;
import com.mtritran.e_commerce_order_service.exception.AppException;
import com.mtritran.e_commerce_order_service.exception.ErrorCode;
import com.mtritran.e_commerce_order_service.mapper.PermissionMapper;
import com.mtritran.e_commerce_order_service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse createPermission(PermissionRequest request) {
        if (permissionRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }

        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String name) {
        var permission = permissionRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

        permissionRepository.delete(permission);
    }

}
