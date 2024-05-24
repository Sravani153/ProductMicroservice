package com.example.microservice.orderservice.controller;

import com.example.microservice.orderservice.Enum.OrderStatus;
import com.example.microservice.orderservice.Enum.PaymentMode;
import com.example.microservice.orderservice.dto.OrderRequest;
import com.example.microservice.orderservice.dto.PlaceOrderResponseDto;
import com.example.microservice.orderservice.entity.Order;
import com.example.microservice.orderservice.entity.OrderedItems;
import com.example.microservice.orderservice.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    MockMvc mockMvc;

    @Mock
    OrderService orderService;

    @InjectMocks
    OrderController orderController;

    ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void testPlaceOrder_Success() throws Exception {
        PlaceOrderResponseDto placeOrderResponseDto = new PlaceOrderResponseDto();
        when(orderService.placeOrder(any(OrderRequest.class))).thenReturn(placeOrderResponseDto);

        mockMvc.perform(post("/order/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new OrderRequest())))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Order Placed Successfully"));
    }

    @Test
    void testPlaceOrder_Expect() throws Exception {
        when(orderService.placeOrder(any(OrderRequest.class))).thenReturn(null);

        mockMvc.perform(post("/order/")
                        .content(objectMapper.writeValueAsString(new OrderRequest()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetOrdersByUserId_Success() throws Exception {
        List<OrderedItems> orderedItems = new ArrayList<>();
        Order sampleOrder = new Order(1L, "Sample Item", OrderStatus.PLACED, LocalDateTime.now(), PaymentMode.DEBIT_CARD, orderedItems, 1L);
        List<Order> orders = List.of(sampleOrder);
        when(orderService.getOrdersByUserId(anyLong())).thenReturn(orders);

        mockMvc.perform(get("/order/user/{userId}", 1L))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].orderNumber").value(sampleOrder.getOrderNumber()))
                .andExpect(jsonPath("$[0].orderStatus").value(sampleOrder.getOrderStatus().toString()));
    }

    @Test
    void testGetOrdersByUserId_Expect() throws Exception {
        when(orderService.getOrdersByUserId(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/order/user/{userId}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetOrdersByProductType_Success() throws Exception {
        List<OrderedItems> orderedItems = new ArrayList<>();
        Order sampleOrder = new Order(1L, "Sample Item", OrderStatus.PLACED, LocalDateTime.now(), PaymentMode.DEBIT_CARD, orderedItems, 1L);
        List<Order> orders = List.of(sampleOrder);
        when(orderService.getOrdersByProductType(anyLong())).thenReturn(orders);

        mockMvc.perform(get("/order/productType/{id}", 1L))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].orderNumber").value(sampleOrder.getOrderNumber()))
                .andExpect(jsonPath("$[0].orderStatus").value(sampleOrder.getOrderStatus().toString()));
    }

    @Test
    void testGetOrdersByProductType_Expect() throws Exception {
        when(orderService.getOrdersByProductType(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/order/productType/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllOrders_Success() throws Exception {
        List<OrderedItems> orderedItems = new ArrayList<>();
        Order sampleOrder = new Order(1L, "Sample Item", OrderStatus.PLACED, LocalDateTime.now(), PaymentMode.DEBIT_CARD, orderedItems, 1L);
        List<Order> orders = List.of(sampleOrder);
        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get("/order/getAll"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].orderNumber").value(sampleOrder.getOrderNumber()))
                .andExpect(jsonPath("$[0].orderStatus").value(sampleOrder.getOrderStatus().toString()));
    }

    @Test
    void testGetAllOrders_Expect() throws Exception {
        when(orderService.getAllOrders()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/order/getAll"))
                .andExpect(status().isNotFound());
    }
}
