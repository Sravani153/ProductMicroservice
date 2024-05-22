package com.example.task.restproductapi.controller;

import com.example.task.restproductapi.Dto.InventoryResponse;
import com.example.task.restproductapi.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inventory")
@Slf4j
public class InventoryController {
    private final InventoryService inventoryService;
    @GetMapping("/checkStock")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam("name") List<String> names) {
        log.info("Received inventory check request for skuCode: {}", names);
        return inventoryService.isInStock(names);
    }

}
