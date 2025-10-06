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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Product product = productRepository.findByName(request.getProductName())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        Order cart = orderRepository.findByUserAndStatus(user, "CART")
                .orElse(Order.builder()
                        .user(user)
                        .status("CART")
                        .createdAt(LocalDateTime.now())
                        .items(new HashSet<>())
                        .build());

        Optional<OrderItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // nếu tăng thêm thì check stock trước
            if (product.getStock() < request.getQuantity()) {
                throw new AppException(ErrorCode.OUT_OF_STOCK);
            }
            existingItem.get().setQuantity(existingItem.get().getQuantity() + request.getQuantity());
            product.setStock(product.getStock() - request.getQuantity());
        } else {
            if (product.getStock() < request.getQuantity()) {
                throw new AppException(ErrorCode.OUT_OF_STOCK);
            }
            OrderItem newItem = OrderItem.builder()
                    .order(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .price(product.getPrice())
                    .build();
            cart.getItems().add(newItem);
            product.setStock(product.getStock() - request.getQuantity());
        }

        productRepository.save(product);
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

        OrderItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getName().equalsIgnoreCase(productName))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        // cộng lại stock khi remove
        Product product = item.getProduct();
        product.setStock(product.getStock() + item.getQuantity());
        productRepository.save(product);

        cart.getItems().remove(item);
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

        Product product = item.getProduct();

        if (quantity <= 0) {
            // remove luôn nếu số lượng <= 0
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
            cart.getItems().remove(item);
        } else {
            int diff = quantity - item.getQuantity();

            if (diff > 0) { // tăng số lượng
                if (product.getStock() < diff) {
                    throw new AppException(ErrorCode.OUT_OF_STOCK);
                }
                item.setQuantity(quantity);
                product.setStock(product.getStock() - diff);
            } else if (diff < 0) { // giảm số lượng
                item.setQuantity(quantity);
                product.setStock(product.getStock() + Math.abs(diff));
            }
            productRepository.save(product);
        }

        orderRepository.save(cart);
        return mapToCartResponse(cart);
    }

    public CartResponse checkout() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Order cart = orderRepository.findByUserAndStatus(user, "CART")
                .orElseThrow(() -> new AppException(ErrorCode.CART_EMPTY));

        if (cart.getItems().isEmpty()) {
            throw new AppException(ErrorCode.CART_EMPTY);
        }

        // đổi trạng thái sang ORDERED
        cart.setStatus("ORDERED");
        cart.setCreatedAt(LocalDateTime.now());

        orderRepository.save(cart);

        // trả về thông tin đơn hàng đã đặt
        return mapToCartResponse(cart);
    }
}



