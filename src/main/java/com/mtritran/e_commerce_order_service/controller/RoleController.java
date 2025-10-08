package com.mtritran.e_commerce_order_service.controller;

import com.mtritran.e_commerce_order_service.dto.request.RoleRequest;
import com.mtritran.e_commerce_order_service.dto.response.ApiResponse;
import com.mtritran.e_commerce_order_service.dto.response.RoleResponse;
import com.mtritran.e_commerce_order_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .code(200)
                .message("Success")
                .result(roleService.createRole(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .code(200)
                .message("Success")
                .result(roleService.getAllRoles())
                .build();
    }

    @GetMapping("/{name}")
    public ApiResponse<RoleResponse> getRole(@PathVariable String name) {
        return ApiResponse.<RoleResponse>builder()
                .code(200)
                .message("Success")
                .result(roleService.getRole(name))
                .build();
    }

    @PutMapping("/{name}")
    public ApiResponse<RoleResponse> updateRole(@PathVariable String name,
                                                @RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .code(200)
                .message("Updated successfully")
                .result(roleService.updateRole(name, request))
                .build();
    }

    @DeleteMapping("/{name}")
    public ApiResponse<Void> deleteRole(@PathVariable String name) {
        roleService.deleteRole(name);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Deleted successfully")
                .build();
    }
}
