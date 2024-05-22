package com.example.task.restproductapi.controller;

import com.example.task.restproductapi.exceptions.NotFoundException;
import com.example.task.restproductapi.entities.ProductType;
import com.example.task.restproductapi.service.ProductTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productType")
@RequiredArgsConstructor
public class ProductTypeController {

    private final ProductTypeService productTypeService;
    private static final String PRODUCT_TYPE_NOT_FOUND_MESSAGE = "ProductType not found with id: ";

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<ProductType> createProductType(@RequestBody ProductType productType) {
        ProductType createdProductType = productTypeService.createProductType(productType);
        if (createdProductType == null) {
            throw new NotFoundException("Failed to create productType.");
        } else {
            return new ResponseEntity<>(createdProductType, HttpStatus.CREATED);
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/")
    public ResponseEntity<List<ProductType>> getAllProductTypes() {
        List<ProductType> productTypes = productTypeService.getAllProductTypes();
        if (productTypes.isEmpty()) {
            throw new NotFoundException("No products found.");
        } else {
            return new ResponseEntity<>(productTypes, HttpStatus.OK);
        }
    }
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ProductType> getProductTypeById(@PathVariable Long id) {
        ProductType productType = productTypeService.getProductTypeById(id);
        if (productType == null) {
            throw new NotFoundException(PRODUCT_TYPE_NOT_FOUND_MESSAGE + id);
        } else {
            return new ResponseEntity<>(productType, HttpStatus.OK);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductType> updateProductType(@PathVariable Long id, @RequestBody ProductType productType) {
        ProductType updatedProductType = productTypeService.updateProductType(id, productType);
        if (updatedProductType == null) {
            throw new NotFoundException(PRODUCT_TYPE_NOT_FOUND_MESSAGE + id);
        } else {
            return new ResponseEntity<>(updatedProductType, HttpStatus.OK);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductType(@PathVariable Long id) {
        try {
            productTypeService.deleteProductType(id);
            return new ResponseEntity<>("Successfully Deleted", HttpStatus.OK);
        } catch (Exception e) {
            throw new NotFoundException(PRODUCT_TYPE_NOT_FOUND_MESSAGE + id);
        }
    }

}
