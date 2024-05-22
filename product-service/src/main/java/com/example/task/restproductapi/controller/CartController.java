package com.example.task.restproductapi.controller;

import com.example.task.restproductapi.Dto.AddCartRequestDto;
import com.example.task.restproductapi.Dto.CartResponseDto;
import com.example.task.restproductapi.Dto.UpdateCartRequestDto;
import com.example.task.restproductapi.exceptions.InvalidInputException;
import com.example.task.restproductapi.mapper.CartMapper;
import com.example.task.restproductapi.service.CartService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private final CartMapper cartMapper;

    @GetMapping("get/{cartId}")
    public ResponseEntity<CartResponseDto> getById(@PathVariable Long cartId) {
        return new ResponseEntity<>(cartMapper.cartToCartResponseDto(cartService.getCartById(cartId)), HttpStatus.OK);
    }

    @GetMapping("get/user/{userId}")
    public ResponseEntity<List<CartResponseDto>> getByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(cartService.getCartByUserId(userId), HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<CartResponseDto> add(@RequestBody AddCartRequestDto cartRequestDto) {
        return new ResponseEntity<>(cartService.add(cartRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("update/{cartId}")
    public ResponseEntity<CartResponseDto> update(@PathVariable Long cartId, @RequestBody UpdateCartRequestDto cartRequestDto) {
        if (!cartId.equals(cartRequestDto.getId())) {
            throw new InvalidInputException("Invalid CartId");
        }
        return new ResponseEntity<>(cartService.update(cartId, cartRequestDto), HttpStatus.OK);
    }

    @DeleteMapping("delete/{cartId}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long cartId) {
        cartService.delete(cartId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
