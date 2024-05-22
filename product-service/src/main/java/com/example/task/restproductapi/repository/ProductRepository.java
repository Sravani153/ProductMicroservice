package com.example.task.restproductapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.task.restproductapi.entities.Product;
import com.example.task.restproductapi.entities.ProductType;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByType_Id(Long id);
    List<Product> findByNameIn(List<String> names);
//    List<Product> findByNameInAndType(List<String> names, ProductType type);




}
