package com.example.task.restproductapi.service;

import java.util.List;

import com.example.task.restproductapi.Dto.InventoryResponse;
import com.example.task.restproductapi.entities.Product;
import com.example.task.restproductapi.entities.ProductType;

public interface ProductService {
 
    Product createProduct(Product product);
 
    List<Product> getAllProducts();
 
    Product getProductById(Long id);
 
    Product updateProduct(Long productId, Product product);
 
    void deleteProduct(Long  id);

    List<Product> getAllProductByType(Long typeId);



}
