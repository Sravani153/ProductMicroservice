package com.example.task.restproductapi.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.task.restproductapi.exceptions.NotFoundException;
import com.example.task.restproductapi.entities.Product;
import com.example.task.restproductapi.service.ProductService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
@Slf4j
public class ProductController {
    
    private final ProductService productService;
    private static final String PRODUCT_NOT_FOUND_MESSAGE = "Product not found with id: ";
    private static final String PRODUCT_TYPE_NOT_FOUND_MESSAGE = "ProductType not found with id: ";


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = this.productService.createProduct(product);
        if (createdProduct == null) {
            throw new NotFoundException("Failed to create product.");
        } else {
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/")
    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> allProducts = this.productService.getAllProducts();
        if (allProducts.isEmpty()) {
            throw new NotFoundException("No products found.");
        } else {
            return new ResponseEntity<>(allProducts, HttpStatus.OK);
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product productById = this.productService.getProductById(id);
        if (productById == null) {
            throw new NotFoundException(PRODUCT_NOT_FOUND_MESSAGE + id);
        } else {
            return new ResponseEntity<>(productById, HttpStatus.OK);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            this.productService.deleteProduct(id);
            return new ResponseEntity<>("Successfully Deleted", HttpStatus.OK);
        } catch (Exception e) {
            throw new NotFoundException(PRODUCT_NOT_FOUND_MESSAGE + id);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updateProduct = this.productService.updateProduct(id, product);
        if (updateProduct == null) {
            throw new NotFoundException(PRODUCT_NOT_FOUND_MESSAGE + id);
        } else {
            return new ResponseEntity<>(updateProduct, HttpStatus.OK);
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/type/{id}")
    public ResponseEntity<List<Product>> getProductByProductTypeId(@PathVariable Long id) {
        List<Product> allProductByType = this.productService.getAllProductByType(id);
        if (allProductByType.isEmpty()) {
            throw new NotFoundException(PRODUCT_TYPE_NOT_FOUND_MESSAGE + id);
        } else {
            return new ResponseEntity<>(allProductByType, HttpStatus.OK);
        }
    }

}



