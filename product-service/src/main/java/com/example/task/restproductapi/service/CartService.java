package com.example.task.restproductapi.service;

import com.example.task.restproductapi.Dto.AddCartRequestDto;
import com.example.task.restproductapi.Dto.CartResponseDto;
import com.example.task.restproductapi.Dto.UpdateCartRequestDto;
import com.example.task.restproductapi.entities.Cart;

import java.util.List;

public interface CartService {
    CartResponseDto add(AddCartRequestDto cartRequestDto);

    void delete(Long cartId);

    CartResponseDto update(Long cartId, UpdateCartRequestDto cartRequestDto);

    Cart getCartById(Long cartId);

    List<CartResponseDto> getCartByUserId(Long userId);
}
