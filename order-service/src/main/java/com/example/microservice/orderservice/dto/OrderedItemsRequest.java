package com.example.microservice.orderservice.dto;

import com.example.microservice.orderservice.Enum.PaymentMode;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderedItemsRequest {
    private Long id;
    private Long productId;
    private String name;
    private BigDecimal price;
    private Long quantity;
}
