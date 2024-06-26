package com.example.task.restproductapi.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCartRequestDto {
    private Long id;

    private Long userId;

    private Long productId;

    private Long quantity;
}
