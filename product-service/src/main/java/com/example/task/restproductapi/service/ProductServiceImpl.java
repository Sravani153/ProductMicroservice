package com.example.task.restproductapi.service;

import java.util.List;

import com.example.task.restproductapi.repository.ProductTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.example.task.restproductapi.exceptions.NotFoundException;
import com.example.task.restproductapi.entities.Product;
import com.example.task.restproductapi.entities.ProductType;
import com.example.task.restproductapi.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;

    private static final String PRODUCT_NOT_FOUND_IN_DB = "No Product Found of this Id in Database ";


    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> notFoundException(PRODUCT_NOT_FOUND_IN_DB));
    }

    public Product updateProduct(Long productId, Product updatedProduct) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(PRODUCT_NOT_FOUND_IN_DB));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setType(updatedProduct.getType());

        return productRepository.save(existingProduct);
    }
    @Override
    public void deleteProduct(Long id) {
        productRepository.delete(getProductById(id));
    }
    @Override
    public List<Product> getAllProductByType(Long typeId) {
        ProductType productType=productTypeRepository.findById(typeId).orElseThrow(() -> new NotFoundException("Products not found with typeId "+typeId));
        return productRepository.findByType_Id(productType.getId());
    }

    private RuntimeException notFoundException(String message) {
        return new NotFoundException(message);
    }
}