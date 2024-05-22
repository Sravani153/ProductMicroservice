package com.example.task.restproductapi.Dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCartRequestDto {

    private Long userId;

    private Long productId;

    private Long quantity;
}
