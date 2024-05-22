package com.example.microservice.orderservice.mapper;

import com.example.microservice.orderservice.dto.OrderItemResponseDto;
import com.example.microservice.orderservice.dto.PlaceOrderResponseDto;
import com.example.microservice.orderservice.entity.Order;
import com.example.microservice.orderservice.entity.OrderedItems;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    PlaceOrderResponseDto orderToPlaceOrderResponseDto(Order order);

    List<OrderItemResponseDto> orderItemRequestDtoToResponseDto(List<OrderedItems> orderedItems);
}
