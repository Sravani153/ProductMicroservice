package com.example.task.restproductapi.service;

import com.example.task.restproductapi.Dto.AddCartRequestDto;
import com.example.task.restproductapi.Dto.CartResponseDto;
import com.example.task.restproductapi.Dto.UpdateCartRequestDto;
import com.example.task.restproductapi.entities.Cart;
import com.example.task.restproductapi.exceptions.FoundException;
import com.example.task.restproductapi.exceptions.NotFoundException;
import com.example.task.restproductapi.entities.Product;
import com.example.task.restproductapi.mapper.CartMapper;
import com.example.task.restproductapi.repository.CartRepository;
import com.example.task.restproductapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final CartMapper cartMapper;

    @Override
    public CartResponseDto add(AddCartRequestDto cartRequestDto) {
        validateUser(cartRequestDto.getUserId());
        var product = validateProductQuantity(cartRequestDto.getProductId(), cartRequestDto.getQuantity());

        var savedCart = cartRepository.save(cartMapper.addCartRequestDtoToCart(cartRequestDto));
        updateProductQuantity(product, -cartRequestDto.getQuantity());

        return cartMapper.cartToCartResponseDto(savedCart);
    }

    @Override
    public void delete(Long cartId) {
        var cart = getCartById(cartId);
        cartRepository.deleteById(cartId);
        validateCartDeletion(cartId);

        var product = getProductById(cart.getProductId());
        updateProductQuantity(product, cart.getQuantity());
    }

    @Override
    public CartResponseDto update(Long cartId, UpdateCartRequestDto cartRequestDto) {
        validateUser(cartRequestDto.getUserId());
        var cart = getCartById(cartId);
        var product = getProductById(cartRequestDto.getProductId());

        if (cart.getProductId().equals(cartRequestDto.getProductId())) {
            adjustProductQuantityForSameProduct(cart, product, cartRequestDto.getQuantity());
        } else {
            validateProductQuantity(cartRequestDto.getProductId(), cartRequestDto.getQuantity());
            updateProductQuantity(getProductById(cart.getProductId()), cart.getQuantity());
            cart.setProductId(cartRequestDto.getProductId());
            updateProductQuantity(product, -cartRequestDto.getQuantity());
        }
        cart.setQuantity(cartRequestDto.getQuantity());

        return cartMapper.cartToCartResponseDto(cartRepository.save(cart));
    }

    private void validateUser(Long userId) {
        userService.getUserById(userId);
    }

    private Product validateProductQuantity(Long productId, Long requestedQuantity) {
        var product = getProductById(productId);
        if (product.getQuantity() < requestedQuantity) {
            throw new NotFoundException("Requested Quantity for the product is not present, product quantity exists only " + product.getQuantity());
        }
        return product;
    }

    public Cart getCartById(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart not found with id: " + cartId));
    }

    @Override
    public List<CartResponseDto> getCartByUserId(Long userId) {
        validateUser(userId);
        return cartRepository.getAllByUserId(userId)
                .stream()
                .map(cartMapper::cartToCartResponseDto)
                .toList();
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id " + productId));
    }

    private void updateProductQuantity(Product product, Long quantityChange) {
        product.setQuantity(product.getQuantity() + quantityChange);
        productRepository.save(product);
    }

    private void validateCartDeletion(Long cartId) {
        if (cartRepository.findById(cartId).isPresent()) {
            throw new FoundException("Cart not deleted");
        }
    }

    private void adjustProductQuantityForSameProduct(Cart cart, Product product, Long newQuantity) {
        long quantityDiff = newQuantity - cart.getQuantity();
        updateProductQuantity(product, -quantityDiff);
        cart.setQuantity(newQuantity);
    }
}
