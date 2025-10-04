package com.mtritran.e_commerce_order_service.service;

import com.mtritran.e_commerce_order_service.dto.request.AddToCartRequest;
import com.mtritran.e_commerce_order_service.dto.response.CartResponse;
import com.mtritran.e_commerce_order_service.dto.response.OrderItemResponse;
import com.mtritran.e_commerce_order_service.entity.Order;
import com.mtritran.e_commerce_order_service.entity.OrderItem;
import com.mtritran.e_commerce_order_service.entity.Product;
import com.mtritran.e_commerce_order_service.entity.User;
import com.mtritran.e_commerce_order_service.exception.AppException;
import com.mtritran.e_commerce_order_service.exception.ErrorCode;
import com.mtritran.e_commerce_order_service.repository.OrderRepository;
import com.mtritran.e_commerce_order_service.repository.ProductRepository;
import com.mtritran.e_commerce_order_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    ProductRepository productRepository;
    UserRepository userRepository;

    public CartResponse addToCart(AddToCartRequest request) {
        // Lấy user hiện tại từ context
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Lấy product theo tên
        Product product = productRepository.findByName(request.getProductName())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        // Lấy giỏ hàng (status = CART) hoặc tạo mới
        Order cart = orderRepository.findByUserAndStatus(user, "CART")
                .orElse(Order.builder()
                        .user(user)
                        .status("CART")
                        .createdAt(LocalDateTime.now())
                        .items(new HashSet<>())
                        .build());

        // Kiểm tra sản phẩm đã có trong giỏ
        Optional<OrderItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + request.getQuantity());
        } else {
            OrderItem newItem = OrderItem.builder()
                    .order(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .price(product.getPrice())
                    .build();
            cart.getItems().add(newItem);
        }

        orderRepository.save(cart);
        return mapToCartResponse(cart);
    }

    public CartResponse viewCart() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Order cart = orderRepository.findByUserAndStatus(user, "CART")
                .orElseThrow(() -> new AppException(ErrorCode.CART_EMPTY));

        return mapToCartResponse(cart);
    }

    private CartResponse mapToCartResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .totalPrice(item.getPrice() * item.getQuantity())
                        .build())
                .toList();

        double totalPrice = items.stream().mapToDouble(OrderItemResponse::getTotalPrice).sum();

        return CartResponse.builder()
                .orderId(order.getId())
                .items(items)
                .totalPrice(totalPrice)
                .build();
    }

    public CartResponse removeFromCart(String productName) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Order cart = orderRepository.findByUserAndStatus(user, "CART")
                .orElseThrow(() -> new AppException(ErrorCode.CART_EMPTY));

        boolean removed = cart.getItems().removeIf(item ->
                item.getProduct().getName().equalsIgnoreCase(productName));

        if (!removed) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        orderRepository.save(cart);
        return mapToCartResponse(cart);
    }

    public CartResponse updateQuantity(String productName, Integer quantity) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Order cart = orderRepository.findByUserAndStatus(user, "CART")
                .orElseThrow(() -> new AppException(ErrorCode.CART_EMPTY));

        OrderItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getName().equalsIgnoreCase(productName))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (quantity <= 0) {
            // nếu số lượng <= 0 thì coi như remove luôn
            cart.getItems().remove(item);
        } else {
            item.setQuantity(quantity);
        }

        orderRepository.save(cart);
        return mapToCartResponse(cart);
    }
}



