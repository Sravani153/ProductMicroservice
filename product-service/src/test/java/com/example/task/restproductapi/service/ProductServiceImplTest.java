package com.example.task.restproductapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.task.restproductapi.exceptions.NotFoundException;
import com.example.task.restproductapi.entities.Product;
import com.example.task.restproductapi.entities.ProductType;
import com.example.task.restproductapi.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    ProductType type;
    Product product;
    List<Product> productList;
    Long id =1L;


    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Product1");

        productList = new ArrayList<>();
        productList.add(new Product());
        productList.add(new Product());

        ProductType type=new ProductType();
        type.setId(1L);
        type.setName("Electronics");
    }

    @Test
    void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.createProduct(product);

        assertNotNull(savedProduct);
        assertEquals(1L, savedProduct.getId());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(productList);

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }


    @Test
    void testUpdateProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");

        assertThrows(NotFoundException.class, () -> productService.updateProduct(1L, updatedProduct));
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    void testUpdateProduct() {
   
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.updateProduct(1L, product);

        assertEquals("Product1", product.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void testGetAllProductByType() {
        when(productRepository.findByType_Id(id)).thenReturn(productList);

        List<Product> result = productService.getAllProductByType(id);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findByType_Id(id);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.deleteProduct(1L));
    }

    @Test
    void testGetAllProductByType_NotFound() {
        when(productRepository.findByType_Id(id)).thenReturn(new ArrayList<>());

        List<Product> result = productService.getAllProductByType(id);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findByType_Id(id);
    }
}
