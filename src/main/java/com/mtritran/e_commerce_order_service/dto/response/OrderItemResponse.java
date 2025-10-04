package com.mtritran.e_commerce_order_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse {
    String productName;
    Integer quantity;
    Double price;
    Double totalPrice;
}

