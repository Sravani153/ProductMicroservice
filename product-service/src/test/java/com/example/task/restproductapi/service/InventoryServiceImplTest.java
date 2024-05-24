package com.example.task.restproductapi.service;

import com.example.task.restproductapi.Dto.InventoryResponse;
import com.example.task.restproductapi.entities.Product;
import com.example.task.restproductapi.repository.ProductRepository;
import com.example.task.restproductapi.repository.ProductTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductTypeRepository productTypeRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private List<Product> products;

    @BeforeEach
    void setUp() {
        Product product1 = new Product();
        product1.setName("item1");
        product1.setQuantity(10L);

        Product product2 = new Product();
        product2.setName("item2");
        product2.setQuantity(0L);

        products = Arrays.asList(product1, product2);
    }

    @Test
    void testIsInStock() {
        List<String> names = Arrays.asList("item1", "item2");

        when(productRepository.findByNameIn(anyList())).thenReturn(products);

        List<InventoryResponse> responses = inventoryService.isInStock(names);

        assertNotNull(responses);
        assertEquals(2, responses.size());

        InventoryResponse response1 = responses.get(0);
        assertEquals("item1", response1.getName());
        assertTrue(response1.isInStock());
        assertEquals(10L, response1.getQuantity());

        InventoryResponse response2 = responses.get(1);
        assertEquals("item2", response2.getName());
        assertFalse(response2.isInStock());
        assertEquals(0L, response2.getQuantity());
    }
}
