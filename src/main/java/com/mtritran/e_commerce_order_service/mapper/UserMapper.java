package com.mtritran.e_commerce_order_service.mapper;

import com.mtritran.e_commerce_order_service.dto.request.UserCreationRequest;
import com.mtritran.e_commerce_order_service.dto.request.UserUpdateRequest;
import com.mtritran.e_commerce_order_service.dto.response.UserResponse;
import com.mtritran.e_commerce_order_service.entity.Role;
import com.mtritran.e_commerce_order_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    User toUser(UserCreationRequest request);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    UserResponse toUserResponse(User user);

    default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}

