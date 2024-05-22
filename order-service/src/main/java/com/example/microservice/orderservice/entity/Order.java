package com.example.microservice.orderservice.entity;

import com.example.microservice.orderservice.Enum.OrderStatus;
import com.example.microservice.orderservice.Enum.PaymentMode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;
    @Column(name = "purchased_date", nullable = false)
    private LocalDateTime purchasedDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode", nullable = false)
    private PaymentMode paymentMode;
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderedItems> orderedItemsList;
    @Column(name = "user_id", nullable = false)
    private Long userId;

}
