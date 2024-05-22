package com.example.task.restproductapi.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryResponse {
    private String name;
    private Long quantity;
    private boolean isInStock;
//    private String productTypeName;
}
