package com.mtritran.e_commerce_order_service.controller;

import com.mtritran.e_commerce_order_service.dto.request.UserCreationRequest;
import com.mtritran.e_commerce_order_service.dto.request.UserUpdateRequest;
import com.mtritran.e_commerce_order_service.dto.response.ApiResponse;
import com.mtritran.e_commerce_order_service.dto.response.UserResponse;
import com.mtritran.e_commerce_order_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("User created successfully");
        response.setResult(userService.createUser(request));
        return response;
    }

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        ApiResponse<List<UserResponse>> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Success");
        response.setResult(userService.getAllUsers());
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable String id) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Success");
        response.setResult(userService.getUserById(id));
        return response;
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserResponse> getMyInfo() {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Success");
        response.setResult(userService.getMyInfo());
        return response;
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String id, @RequestBody UserUpdateRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("User updated successfully");
        response.setResult(userService.updateUser(id, request));
        return response;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("User deleted successfully");
        return response;
    }
}

