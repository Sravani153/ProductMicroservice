package com.example.microservice.orderservice.dto;

import com.example.microservice.orderservice.Enum.PaymentMode;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequest {
    private Long userId;

    private PaymentMode paymentMode;

    private List<OrderedItemsRequest> orderedItemsDtosList;
}
