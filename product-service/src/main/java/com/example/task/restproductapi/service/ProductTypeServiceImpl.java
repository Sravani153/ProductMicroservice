package com.example.task.restproductapi.service;

import com.example.task.restproductapi.exceptions.NotFoundException;
import com.example.task.restproductapi.entities.ProductType;
import com.example.task.restproductapi.repository.ProductTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeRepository productTypeRepository;

    private static final String PRODUCT_TYPE_NOT_FOUND_IN_DB = "No Product Type Found of this Id in Database ";

    @Override
    public ProductType createProductType(ProductType productType) {
        return productTypeRepository.save(productType);
    }

    @Override
    public List<ProductType> getAllProductTypes() {
        return productTypeRepository.findAll();
    }

    @Override
    public ProductType getProductTypeById(Long id) {
        return productTypeRepository.findById(id)
                .orElseThrow(() -> notFoundException(PRODUCT_TYPE_NOT_FOUND_IN_DB));
    }

    @Override
    public ProductType updateProductType(Long productTypeId, ProductType productType) {
        ProductType existingProductType = getProductTypeById(productTypeId);
        existingProductType.setName(productType.getName());
        // existingProductType.setProducts(productType.getProducts());
        return productTypeRepository.save(existingProductType);
    }

    @Override
    public void deleteProductType(Long id) {
        productTypeRepository.delete(getProductTypeById(id));
    }

    private RuntimeException notFoundException(String message) {
        return new NotFoundException(message);
    }
}

