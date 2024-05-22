package com.example.task.restproductapi.service;

import com.example.task.restproductapi.Dto.InventoryResponse;
import com.example.task.restproductapi.entities.Product;
import com.example.task.restproductapi.entities.ProductType;
import com.example.task.restproductapi.repository.ProductRepository;
import com.example.task.restproductapi.repository.ProductTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {
    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    public List<InventoryResponse> isInStock(List<String> names) {
        log.info("Checking Inventory");

        List<Product> products = productRepository.findByNameIn(names);

        // Create a list to store InventoryResponse objects
        List<InventoryResponse> productResponses = new ArrayList<>();

        for (String name : names) {

            Optional<Product> optionalProduct = products.stream()
                    .filter(product -> product.getName().equalsIgnoreCase(name))
                    .findFirst();

            // Check if the product exists and is in stock
            boolean isInStock = optionalProduct.isPresent() && optionalProduct.get().getQuantity() > 0;
            Long quantity=optionalProduct.get().getQuantity();

            // Create a InventoryResponse object and add it to the list
            InventoryResponse productResponse = InventoryResponse.builder()
                    .name(name)
                    .quantity(quantity)
                    .isInStock(isInStock)
                    .build();
            productResponses.add(productResponse);
        }

        return productResponses;
    }


}
