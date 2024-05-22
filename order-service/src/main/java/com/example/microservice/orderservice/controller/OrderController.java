package com.example.microservice.orderservice.controller;

import com.example.microservice.orderservice.dto.OrderRequest;
import com.example.microservice.orderservice.dto.OrderResponse;
import com.example.microservice.orderservice.dto.PlaceOrderResponseDto;
import com.example.microservice.orderservice.entity.Order;
import com.example.microservice.orderservice.service.OrderService;
import com.example.microservice.orderservice.webclient.UserWebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final WebClient.Builder webClientBuilder;
    private  final UserWebClientService userWebClientService;

    /**
     * Endpoint to place an order.
     *
     * @param request The request containing order details.
     * @return ResponseEntity<OrderResponse> Returns HTTP status and order response.
     */
    @PostMapping("/")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest request){

        log.info("{}",request);

        PlaceOrderResponseDto order = orderService.placeOrder(request);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderResponse("Failed to place order.", null));
        }

        String message = "Order Placed Successfully";
        OrderResponse orderResponse = new OrderResponse(message, order);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    /**
     * Endpoint to retrieve orders by user ID.
     *
     * @param userId The ID of the user.
     * @return ResponseEntity<List<Order>> Returns HTTP status and list of orders.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        if (orders.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(orders);
        }
    }


    @GetMapping("/productType/{id}")
    public ResponseEntity<List<Order>> getOrdersByProductType(@PathVariable Long id) {
        List<Order> orders = orderService.getOrdersByProductType(id);
        if (orders.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(orders);
        }
    }

    /**
     * Endpoint to retrieve all orders.
     *
     * @return ResponseEntity<List<Order>> Returns HTTP status and list of all orders.
     */
    @GetMapping("/getall")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(orders);
        }
    }
}
