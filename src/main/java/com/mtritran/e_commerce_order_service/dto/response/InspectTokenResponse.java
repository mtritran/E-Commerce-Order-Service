package com.mtritran.e_commerce_order_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InspectTokenResponse {
    boolean valid;
    String subject;
    Date expiry;
}
