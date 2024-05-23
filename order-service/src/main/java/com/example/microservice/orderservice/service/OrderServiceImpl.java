package com.example.microservice.orderservice.service;

import com.example.microservice.orderservice.Enum.OrderStatus;
import com.example.microservice.orderservice.Exception.NotFoundException;
import com.example.microservice.orderservice.dto.ProductDto;
import com.example.microservice.orderservice.dto.UserDto;
import com.example.microservice.orderservice.dto.OrderItemResponseDto;
import com.example.microservice.orderservice.dto.OrderRequest;
import com.example.microservice.orderservice.dto.PlaceOrderResponseDto;
import com.example.microservice.orderservice.dto.OrderedItemsRequest;
import com.example.microservice.orderservice.dto.InventoryResponse;
import com.example.microservice.orderservice.mapper.OrderMapper;
import com.example.microservice.orderservice.entity.Order;
import com.example.microservice.orderservice.entity.OrderedItems;
import com.example.microservice.orderservice.repo.OrderRepo;
import com.example.microservice.orderservice.webclient.ProductWebClientService;
import com.example.microservice.orderservice.webclient.UserWebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepo orderRepo;

    private final ProductWebClientService productWebClientService;

    private final UserWebClientService userWebClientService;

    private final OrderMapper orderMapper;

    public PlaceOrderResponseDto placeOrder(OrderRequest request) {

        UserDto userDto = userWebClientService.getUserById(request.getUserId());

        if (userDto == null) {
            throw new NotFoundException("User not found with ID: " + request.getUserId());
        }

        Order order = new Order();

        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderStatus(OrderStatus.PLACED);
        order.setPurchasedDate(LocalDateTime.now());
        order.setPaymentMode(request.getPaymentMode());
        order.setUserId(request.getUserId());

        List<OrderedItems> orderedItems = new ArrayList<>();
        List<OrderedItemsRequest> orderedItemsList = request.getOrderedItemsDtosList();
        if (orderedItemsList != null) {
            orderedItemsList.forEach(orderedItemsRequest -> {
                ProductDto product = productWebClientService.getProductById(orderedItemsRequest.getProductId());
                if (product == null) {
                    throw new NotFoundException("Product not found with ID: " + orderedItemsRequest.getProductId());
                }
                if (product.getQuantity() < orderedItemsRequest.getQuantity()) {
                    throw new NotFoundException("Requested Quantity is greater than original Product stock for Product: " + product.getName());
                }
            });
            orderedItems = orderedItemsList.stream()
                    .map(this::mapToOrderItemsReq)
                    .toList();
        }
        order.setOrderedItemsList(orderedItems);

        List<String> names = orderedItemsList.stream()
                .map(OrderedItemsRequest::getName)
                .toList();

        log.info("SKU codes extracted from the order: {}", names);

        InventoryResponse[] productResponses = productWebClientService.checkStock(names);

        if (productResponses == null || productResponses.length == 0) {
            throw new IllegalStateException("Failed to retrieve inventory information.");
        }

        for (OrderedItems orderedItem : order.getOrderedItemsList()) {
            String name = orderedItem.getName();
            Optional<InventoryResponse> productResponse = Arrays.stream(productResponses)
                    .filter(response -> response != null && response.getName().equals(name))
                    .findFirst();

            if (productResponse.isEmpty() || !productResponse.get().isInStock()) {
                throw new IllegalArgumentException("Product with SKU: " + name + " is out of stock!");
            }

            Long orderedQuantity = orderedItem.getQuantity();
            Long availableQuantity = productResponse.get().getQuantity();
            if (orderedQuantity > availableQuantity) {
                throw new IllegalArgumentException("Insufficient quantity for product with SKU: " + name);
            }
        }
        var savedOrder = orderRepo.save(order);

        var response = orderMapper.orderToPlaceOrderResponseDto(savedOrder);
        List<OrderItemResponseDto> orderItemResponseDtos = orderMapper.orderItemRequestDtoToResponseDto(savedOrder.getOrderedItemsList());

        orderItemResponseDtos.forEach(orderItemResponseDto -> {
            ProductDto product = productWebClientService.getProductById(orderItemResponseDto.getProductId());
            orderItemResponseDto.setProductType(product.getType());
            product.setQuantity(product.getQuantity() - orderItemResponseDto.getQuantity());
            ProductDto updatedProduct = productWebClientService.updateProductById(product.getId(), product);
            if (updatedProduct == null) {
                throw new NotFoundException("PRODUCT_QUANTITY_NOT_UPDATED");
            }
        });
        response.setOrderedItemsList(orderItemResponseDtos);
        return response;
    }


    @Override
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public List<Order> getOrdersByUserId(Long userId) {
        UserDto userDto = userWebClientService.getUserById(userId);

        if (userDto != null) {
            return orderRepo.findByUserId(userDto.getId());
        } else {
            throw new NotFoundException("User not found with ID: " + userId);
        }
    }

    private OrderedItems mapToOrderItemsReq(OrderedItemsRequest itemsRequest) {
        OrderedItems orderedItems = new OrderedItems();
        orderedItems.setQuantity(itemsRequest.getQuantity());
        orderedItems.setPrice(itemsRequest.getPrice());
        orderedItems.setName(itemsRequest.getName());
        orderedItems.setProductId(itemsRequest.getProductId());
        return orderedItems;
    }


    @Override
    public List<Order> getOrdersByProductType(Long id) {
        var productTypeDto = productWebClientService.getProductTypeById(id);

        if (productTypeDto != null) {
            return orderRepo.findByUserId(productTypeDto.getId());
        } else {
            throw new NotFoundException("User not found with ID: " + id);
        }
    }
}
