package com.example.microservice.orderservice.service;

import com.example.microservice.orderservice.dto.OrderRequest;
import com.example.microservice.orderservice.dto.PlaceOrderResponseDto;
import com.example.microservice.orderservice.entity.Order;

import java.util.List;

public interface OrderService {
     PlaceOrderResponseDto placeOrder(OrderRequest request);
     List<Order> getAllOrders();
     List<Order> getOrdersByUserId(Long userId);
    List<Order> getOrdersByProductType(Long id);

}
