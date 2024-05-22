package com.example.task.restproductapi.controller;

import com.example.task.restproductapi.Dto.AddCartRequestDto;
import com.example.task.restproductapi.Dto.CartResponseDto;
import com.example.task.restproductapi.Dto.UpdateCartRequestDto;
import com.example.task.restproductapi.entities.Cart;
import com.example.task.restproductapi.service.CartService;
import com.example.task.restproductapi.mapper.CartMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    MockMvc mockMvc;

    @Mock
    CartService cartService;

    @InjectMocks
    CartController cartController;

    @Mock
    private CartMapper cartMapper;

    ObjectMapper objectMapper = new ObjectMapper();

    UpdateCartRequestDto updateCartRequestDto = UpdateCartRequestDto.builder()
            .id(2L)
            .userId(1L)
            .productId(2L)
            .quantity(4L)
            .build();

    @Test
    void testGetById_ValidCartId_ReturnsCartResponseDto() throws Exception {
        Long cartId = 1L;
        CartResponseDto cartResponseDto = new CartResponseDto();
        cartResponseDto.setId(cartId);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
        Mockito.when(cartService.getCartById(cartId)).thenReturn(new Cart());
        Mockito.when(cartMapper.cartToCartResponseDto(any())).thenReturn(cartResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/cart/get/{cartId}", cartId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(cartId));
    }

    @Test
    void testGetByUserId_ValidUserId_ReturnsCartResponseDtoList() throws Exception {
        Long userId = 1L;
        CartResponseDto cartResponseDto = new CartResponseDto();
        Mockito.when(cartService.getCartByUserId(userId)).thenReturn(Collections.singletonList(cartResponseDto));
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/cart/get/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void testAdd_ValidAddCartRequestDto_ReturnsCreatedResponseEntity() throws Exception {
        CartResponseDto cartResponseDto = new CartResponseDto();
        AddCartRequestDto addCartRequestDto = AddCartRequestDto.builder()
                .productId(1L)
                .userId(1L)
                .quantity(2L)
                .build();
        Mockito.when(cartService.add(any(AddCartRequestDto.class))).thenReturn(cartResponseDto);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addCartRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdate_ValidUpdateCartRequestDto_ReturnsOkResponseEntity() throws Exception {
        Long cartId = 2L;
        CartResponseDto cartResponseDto = new CartResponseDto();
        Mockito.when(cartService.update(eq(cartId), any(UpdateCartRequestDto.class))).thenReturn(cartResponseDto);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();

        mockMvc.perform(MockMvcRequestBuilders.put("/cart/update/{cartId}", cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCartRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdate_InvalidCartId_ThrowsInvalidInputException() throws Exception {
        Long cartId = 1L;
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();

        mockMvc.perform(MockMvcRequestBuilders.put("/cart/update/{cartId}", cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCartRequestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete_ValidCartId_ReturnsNoContent() throws Exception {
        Long cartId = 1L;
        Mockito.doNothing().when(cartService).delete(cartId);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();

        mockMvc.perform(MockMvcRequestBuilders.delete("/cart/delete/{cartId}", cartId))
                .andExpect(status().isNoContent());
    }
}
