package com.example.task.restproductapi.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.task.restproductapi.Enum.UserRole;

class EntitiesTest {
    
    @Test
     void testProductEntity() {
        ProductType type = new ProductType();
        type.setId(1L);
        type.setName("Electronics");

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setDescription("A powerful laptop");
        product.setPrice(1000.00);
        product.setType(type);

        assertEquals(1L, product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals("A powerful laptop", product.getDescription());
        assertEquals(1000.00, product.getPrice());
        assertEquals(type, product.getType());
    }

    @Test
     void testProductTypeEntity() {
        ProductType type = new ProductType();
        type.setId(1L);
        type.setName("Electronics");

        assertEquals(1L, type.getId());
        assertEquals("Electronics", type.getName());
    }

    @Test
     void testUserEntity() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("password");
        user.setRole(UserRole.USER);

        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals(UserRole.USER, user.getRole());
    }
}
