package com.mtritran.e_commerce_order_service.mapper;

import com.mtritran.e_commerce_order_service.dto.request.PermissionRequest;
import com.mtritran.e_commerce_order_service.dto.response.PermissionResponse;
import com.mtritran.e_commerce_order_service.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
