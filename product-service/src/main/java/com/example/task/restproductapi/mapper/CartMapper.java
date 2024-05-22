package com.example.task.restproductapi.mapper;

import com.example.task.restproductapi.Dto.AddCartRequestDto;
import com.example.task.restproductapi.Dto.CartResponseDto;
import com.example.task.restproductapi.Dto.UpdateCartRequestDto;
import com.example.task.restproductapi.entities.Cart;
import org.mapstruct.Mapper;

@Mapper
public interface CartMapper {
    Cart addCartRequestDtoToCart(AddCartRequestDto cartRequestDto);

    CartResponseDto cartToCartResponseDto(Cart cart);

    Cart updateCartRequestDtoToCart(UpdateCartRequestDto updateCartRequestDto);
}
