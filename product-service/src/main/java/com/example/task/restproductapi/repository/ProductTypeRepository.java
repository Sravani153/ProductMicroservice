package com.example.task.restproductapi.repository;

import com.example.task.restproductapi.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.task.restproductapi.entities.ProductType;

import java.util.List;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
//    ProductType findByName(String name);


}
