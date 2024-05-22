package com.example.microservice.orderservice.webclient;

import com.example.microservice.orderservice.dto.InventoryResponse;

import com.example.microservice.orderservice.dto.ProductDto;
import com.example.microservice.orderservice.dto.ProductTypeDto;
import com.example.microservice.orderservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductWebClientService {
    private final WebClient.Builder webClientBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public InventoryResponse[] checkStock(List<String> names) {
        String url = "http://product-service/inventory/checkStock?name=" + String.join(",", names);
        log.info("Request URL: {}", url);

        return webClientBuilder.build().get()
                .uri(url)
                .header("Authorization", "Bearer " + jwtTokenProvider.getToken())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .onErrorResume(error -> {
                    log.error("Failed to retrieve inventory information from response: {}", error.getMessage());
                    return Mono.empty();
                })
                .block();
    }

    public ProductTypeDto getProductTypeById(Long id) {
        return webClientBuilder.build()
                .get()
                .uri("http://product-service/productType/{id}", id)
                .header("Authorization", "Bearer " + jwtTokenProvider.getToken())
                .retrieve()
                .bodyToMono(ProductTypeDto.class)
                .block();
    }

    public ProductDto getProductById(Long id) {
        return webClientBuilder.build()
                .get()
                .uri("http://product-service/api/product/{id}", id)
                .header("Authorization", "Bearer " + jwtTokenProvider.getToken())
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
    }

    public ProductDto updateProductById(Long id, ProductDto productDto) {
        return webClientBuilder.build()
                .put()
                .uri("http://product-service/api/product/{id}", id)
                .header("Authorization", "Bearer " + jwtTokenProvider.getToken())
                .bodyValue(productDto)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
    }
}
