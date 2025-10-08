package com.mtritran.e_commerce_order_service.controller;

import com.mtritran.e_commerce_order_service.dto.request.ProductRequest;
import com.mtritran.e_commerce_order_service.dto.response.ApiResponse;
import com.mtritran.e_commerce_order_service.dto.response.ProductResponse;
import com.mtritran.e_commerce_order_service.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Product", description = "APIs for managing products")
public class ProductController {

    ProductService productService;

    @PostMapping
    @Operation(summary = "Create a new product")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProductResponse> create(@RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("Created successfully")
                .result(productService.create(request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all products")
    public ApiResponse<List<ProductResponse>> getAll() {
        return ApiResponse.<List<ProductResponse>>builder()
                .code(200)
                .message("Success")
                .result(productService.getAll())
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    @PreAuthorize("permitAll()")
    public ApiResponse<ProductResponse> getById(@PathVariable String id) {
        return ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("Success")
                .result(productService.getById(id))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product by ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProductResponse> update(@PathVariable String id,
                                               @RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("Updated successfully")
                .result(productService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product by ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable String id) {
        productService.delete(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Deleted successfully")
                .build();
    }
}