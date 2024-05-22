package com.example.task.restproductapi.repository;

import com.example.task.restproductapi.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> getAllByUserId(Long userId);
}
