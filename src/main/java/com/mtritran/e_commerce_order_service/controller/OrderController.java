package com.mtritran.e_commerce_order_service.controller;

import com.mtritran.e_commerce_order_service.dto.request.AddToCartRequest;
import com.mtritran.e_commerce_order_service.dto.response.ApiResponse;
import com.mtritran.e_commerce_order_service.dto.response.CartResponse;
import com.mtritran.e_commerce_order_service.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping("/cart/add")
    public ApiResponse<CartResponse> addToCart(@RequestBody AddToCartRequest request) {
        return ApiResponse.<CartResponse>builder()
                .code(200)
                .message("Added to cart successfully")
                .result(orderService.addToCart(request))
                .build();
    }

    @GetMapping("/cart")
    public ApiResponse<CartResponse> viewCart() {
        return ApiResponse.<CartResponse>builder()
                .code(200)
                .message("Cart retrieved successfully")
                .result(orderService.viewCart())
                .build();
    }

    @DeleteMapping("/cart/remove/{productName}")
    public ApiResponse<CartResponse> removeFromCart(@PathVariable String productName) {
        return ApiResponse.<CartResponse>builder()
                .code(200)
                .message("Product removed from cart successfully")
                .result(orderService.removeFromCart(productName))
                .build();
    }

    @PutMapping("/cart/update")
    public ApiResponse<CartResponse> updateQuantity(@RequestBody AddToCartRequest request) {
        return ApiResponse.<CartResponse>builder()
                .code(200)
                .message("Cart updated successfully")
                .result(orderService.updateQuantity(request.getProductName(), request.getQuantity()))
                .build();
    }
}
