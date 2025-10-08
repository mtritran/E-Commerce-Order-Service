package com.mtritran.e_commerce_order_service.mapper;

import com.mtritran.e_commerce_order_service.dto.request.RoleRequest;
import com.mtritran.e_commerce_order_service.dto.response.RoleResponse;
import com.mtritran.e_commerce_order_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
