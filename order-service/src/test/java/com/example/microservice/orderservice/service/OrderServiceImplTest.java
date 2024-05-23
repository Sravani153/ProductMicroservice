package com.example.microservice.orderservice.service;

import com.example.microservice.orderservice.Enum.OrderStatus;
import com.example.microservice.orderservice.Enum.PaymentMode;
import com.example.microservice.orderservice.Exception.NotFoundException;
import com.example.microservice.orderservice.dto.*;
import com.example.microservice.orderservice.entity.Order;
import com.example.microservice.orderservice.mapper.OrderMapper;
import com.example.microservice.orderservice.repo.OrderRepo;
import com.example.microservice.orderservice.webclient.ProductWebClientService;
import com.example.microservice.orderservice.webclient.UserWebClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    OrderServiceImpl orderService;

    @Mock
    OrderRepo orderRepo;

    @Mock
    UserWebClientService userWebClientService;

    @Mock
    ProductWebClientService productWebClientService;

    @Mock
    OrderMapper orderMapper;

    private OrderRequest orderRequest;
    private PlaceOrderResponseDto expectedResponse;

    @BeforeEach
    void setUp() {
        orderRequest = new OrderRequest();
        orderRequest.setUserId(1L);
        orderRequest.setOrderedItemsDtosList(Arrays.asList(
                new OrderedItemsRequest(1L, 1L, "product1", BigDecimal.valueOf(10), 1L),
                new OrderedItemsRequest(2L, 1L, "product2", BigDecimal.valueOf(20), 2L)
        ));

        expectedResponse = new PlaceOrderResponseDto();
        expectedResponse.setOrderNumber("1234567890");
        expectedResponse.setOrderStatus(OrderStatus.PLACED);
        expectedResponse.setPurchasedDate(LocalDateTime.now());
        expectedResponse.setPaymentMode(PaymentMode.CASH);
        expectedResponse.setUserId(1L);
        expectedResponse.setOrderedItemsList(Arrays.asList(
                new OrderItemResponseDto(1L, 1L, new ProductTypeDto(1L, "Home"), "product1", BigDecimal.valueOf(10), 100L),
                new OrderItemResponseDto(2L, 1L, new ProductTypeDto(1L, "Home"), "product2", BigDecimal.valueOf(20), 200L)
        ));
    }

    @Test
    void testPlaceOrder_ValidOrder_ReturnsCorrectResponse() {
        // Arrange
        ProductDto productDtoRequest1 = new ProductDto(1L, "product1", "", 100.0, 100L, new ProductTypeDto(1L, "productType1"));
        ProductDto productDtoResponse1 = new ProductDto(1L, "product1", "", 100.0, 99L, new ProductTypeDto(1L, "productType1")); // After reduction
        UserDto userDto = new UserDto(1L, "John Doe", "john.doe@example.com");
        InventoryResponse[] inventoryResponses = new InventoryResponse[]{
                new InventoryResponse("product1", 100L, true),
                new InventoryResponse("product2", 200L, true)
        };

        when(userWebClientService.getUserById(anyLong())).thenReturn(userDto);
        when(productWebClientService.getProductById(1L)).thenReturn(productDtoRequest1);
        when(productWebClientService.checkStock(any())).thenReturn(inventoryResponses);
        when(orderMapper.orderToPlaceOrderResponseDto(any())).thenReturn(expectedResponse);
        when(orderMapper.orderItemRequestDtoToResponseDto(any())).thenReturn(expectedResponse.getOrderedItemsList());
        when(orderRepo.save(any())).thenReturn(new Order());
        when(productWebClientService.updateProductById(eq(1L), any(ProductDto.class))).thenReturn(productDtoResponse1);

        // Act
        PlaceOrderResponseDto response = orderService.placeOrder(orderRequest);

        // Assert
        assertEquals(expectedResponse, response);
        verify(userWebClientService, times(1)).getUserById(anyLong());
        verify(productWebClientService, times(4)).getProductById(anyLong());
        verify(productWebClientService, times(1)).checkStock(any());
        verify(productWebClientService, times(2)).updateProductById(anyLong(), any(ProductDto.class));
        verify(orderMapper, times(1)).orderToPlaceOrderResponseDto(any());
        verify(orderMapper, times(1)).orderItemRequestDtoToResponseDto(any());
        verify(orderRepo, times(1)).save(any());
    }


    @Test
    void testPlaceOrder_InvalidUser_ThrowsNotFoundException() {
        // Given
        when(userWebClientService.getUserById(any())).thenReturn(null);

        // When & Then
        assertThrows(NotFoundException.class, () -> orderService.placeOrder(orderRequest));
        verify(userWebClientService, times(1)).getUserById(any());
    }

    @Test
    void testPlaceOrder_InvalidProduct_ThrowsNotFoundException() {
        when(userWebClientService.getUserById(anyLong())).thenReturn(new UserDto());
        when(productWebClientService.getProductById(anyLong())).thenReturn(null);

        // When & Then
        assertThrows(NotFoundException.class, () -> orderService.placeOrder(orderRequest));
        verify(userWebClientService, times(1)).getUserById(anyLong());
        verify(productWebClientService, times(1)).getProductById(anyLong());
    }

    @Test
    void testPlaceOrder_InsufficientProductQuantity_ThrowsIllegalArgumentException() {
        when(userWebClientService.getUserById(anyLong())).thenReturn(new UserDto());
        when(productWebClientService.getProductById(1L)).thenReturn(new ProductDto(1L, "product1", "", 100.0, 50L, new ProductTypeDto(1L, "")));
        when(productWebClientService.getProductById(2L)).thenReturn(new ProductDto(2L, "product2", "", 200.0, 200L, new ProductTypeDto(2L, "")));
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(1L);
        orderRequest.setPaymentMode(PaymentMode.CREDIT_CARD);
        orderRequest.setOrderedItemsDtosList(List.of(
                new OrderedItemsRequest(1L, 1L, "product1", BigDecimal.valueOf(60), 1L),
                new OrderedItemsRequest(2L, 2L, "product2", BigDecimal.valueOf(200), 2L)
        ));

        // When & Then
        assertThrows(IllegalStateException.class, () -> orderService.placeOrder(orderRequest));
        verify(productWebClientService, times(2)).getProductById(anyLong());
        verify(productWebClientService, times(1)).checkStock(any());
        verify(orderMapper, times(0)).orderToPlaceOrderResponseDto(any());
        verify(orderMapper, times(0)).orderItemRequestDtoToResponseDto(any());
    }

    @Test
    void test_getAllOrders_Success() {
        List<Order> orders = new ArrayList<>();
        when(orderRepo.findAll()).thenReturn(orders);

        orderService.getAllOrders();

        verify(orderRepo, times(1)).findAll();
    }

    @Test
    void test_getOrdersByUserId_Success() {
        Long id = 1L;
        UserDto userDto = new UserDto();
        userDto.setId(id);
        List<Order> orders = new ArrayList<>();
        when(userWebClientService.getUserById(id)).thenReturn(userDto);
        when(orderRepo.findByUserId(id)).thenReturn(orders);

        orderService.getOrdersByUserId(id);

        verify(userWebClientService, times(1)).getUserById(anyLong());
        verify(orderRepo, times(1)).findByUserId(anyLong());
    }

    @Test
    void test_getOrderById_Success_Expect() {
        when(userWebClientService.getUserById(anyLong())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> orderService.getOrdersByUserId(anyLong()));
    }

    @Test
    void test_getOrdersByProductType_Success() {
        Long id = 1L;
        ProductTypeDto productTypeDto = new ProductTypeDto();
        productTypeDto.setId(id);
        List<Order> orders = new ArrayList<>();
        when(productWebClientService.getProductTypeById(id)).thenReturn(productTypeDto);
        when(orderRepo.findByUserId(id)).thenReturn(orders);

        orderService.getOrdersByProductType(id);

        verify(productWebClientService, times(1)).getProductTypeById(anyLong());
        verify(orderRepo, times(1)).findByUserId(anyLong());
    }

    @Test
    void test_getOrdersByProductType_Expect() {
        when(productWebClientService.getProductTypeById(anyLong())).thenReturn(null);

        assertThrows(NotFoundException.class, () ->
                orderService.getOrdersByProductType(anyLong()));
    }

}
