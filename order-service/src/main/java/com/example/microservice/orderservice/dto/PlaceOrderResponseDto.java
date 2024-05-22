package com.example.microservice.orderservice.dto;

import com.example.microservice.orderservice.Enum.OrderStatus;
import com.example.microservice.orderservice.Enum.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceOrderResponseDto {
    private Long id;
    private Long userId;
    private String orderNumber;
    private OrderStatus orderStatus;
    //check
    private PaymentMode paymentMode;
    private List<OrderItemResponseDto> orderedItemsList;
    private LocalDateTime purchasedDate;

}
