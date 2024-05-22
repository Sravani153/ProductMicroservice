package com.example.microservice.orderservice.dto;

import com.example.microservice.orderservice.Enum.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponseDto {
    private Long id;
    private Long productId;
    private ProductTypeDto productType;
    private String name;
    private BigDecimal price;
    private Long quantity;
}
