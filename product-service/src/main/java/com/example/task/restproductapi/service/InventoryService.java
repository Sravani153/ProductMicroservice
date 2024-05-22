package com.example.task.restproductapi.service;

import com.example.task.restproductapi.Dto.InventoryResponse;

import java.util.List;

public interface InventoryService {
    List<InventoryResponse> isInStock(List<String> names);
//    List<InventoryResponse> isInStockByType(List<String> names, String productTypeName);

}
