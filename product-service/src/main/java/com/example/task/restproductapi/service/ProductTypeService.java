package com.example.task.restproductapi.service;

import com.example.task.restproductapi.entities.ProductType;

import java.util.List;
import java.util.Set;

public interface ProductTypeService {

    ProductType createProductType(ProductType productType);

    List<ProductType> getAllProductTypes();

    ProductType getProductTypeById(Long id);

    ProductType updateProductType(Long productTypeId, ProductType productType);

    void deleteProductType(Long id);
}
