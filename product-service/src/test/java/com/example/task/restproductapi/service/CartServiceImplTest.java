package com.example.task.restproductapi.service;

import com.example.task.restproductapi.Dto.AddCartRequestDto;
import com.example.task.restproductapi.Dto.CartResponseDto;
import com.example.task.restproductapi.Dto.UpdateCartRequestDto;
import com.example.task.restproductapi.entities.Cart;
import com.example.task.restproductapi.entities.Product;
import com.example.task.restproductapi.exceptions.FoundException;
import com.example.task.restproductapi.exceptions.NotFoundException;
import com.example.task.restproductapi.mapper.CartMapper;
import com.example.task.restproductapi.repository.CartRepository;
import com.example.task.restproductapi.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserService userService;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    Cart cart;
    Product product;
    CartResponseDto cartResponseDto;

    @BeforeEach
    void setUp() {
        cart = Cart.builder()
                .id(1L)
                .userId(1L)
                .productId(1L)
                .quantity(3L)
                .build();

        product = Product.builder()
                .id(1L)
                .quantity(10L)
                .build();

        cartResponseDto = CartResponseDto.builder()
                .id(1L)
                .build();
    }

    @Test
    void testAdd_ValidAddCartRequestDto_ReturnsCartResponseDto() {
        AddCartRequestDto addCartRequestDto = new AddCartRequestDto();
        addCartRequestDto.setUserId(1L);
        addCartRequestDto.setProductId(1L);
        addCartRequestDto.setQuantity(2L);

        when(userService.getUserById(1L)).thenReturn(null);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartMapper.addCartRequestDtoToCart(any(AddCartRequestDto.class))).thenReturn(cart);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.cartToCartResponseDto(any(Cart.class))).thenReturn(cartResponseDto);

        CartResponseDto result = cartService.add(addCartRequestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testDelete_ValidCartId_DeletesCartAndUpdatesProduct() {
        Long cartId = 1L;

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart), Optional.empty());
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        cartService.delete(cartId);

        verify(cartRepository, times(1)).deleteById(cartId);
        verify(productRepository, times(1)).save(any(Product.class));
    }


    @Test
    void testUpdate_ValidUpdateCartRequestDto_ReturnsCartResponseDto() {
        Long cartId = 1L;
        UpdateCartRequestDto updateCartRequestDto = new UpdateCartRequestDto();
        updateCartRequestDto.setId(cartId);
        updateCartRequestDto.setUserId(1L);
        updateCartRequestDto.setProductId(1L);
        updateCartRequestDto.setQuantity(3L);

        when(userService.getUserById(1L)).thenReturn(null);
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.cartToCartResponseDto(any(Cart.class))).thenReturn(cartResponseDto);

        CartResponseDto result = cartService.update(cartId, updateCartRequestDto);

        assertNotNull(result);
        assertEquals(cartId, result.getId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetCartById_ValidCartId_ReturnsCart() {
        Long cartId = 1L;

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        Cart result = cartService.getCartById(cartId);

        assertNotNull(result);
        assertEquals(cartId, result.getId());
    }

    @Test
    void testGetCartById_InvalidCartId_ThrowsNotFoundException() {
        Long cartId = 1L;

        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cartService.getCartById(cartId));
    }
}

