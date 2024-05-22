package com.example.microservice.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route(predicateSpec -> predicateSpec.path("/api/product/**").uri("lb://product-service"))
                .route(predicateSpec -> predicateSpec.path("/users/**").uri("lb://product-service"))
                .route(predicateSpec -> predicateSpec.path("/cart/**").uri("lb://product-service"))
                .route(predicateSpec -> predicateSpec.path("/inventory/**").uri("lb://product-service"))
                .route(predicateSpec -> predicateSpec.path("/productType/**").uri("lb://product-service"))
                .route(predicateSpec -> predicateSpec.path("/order/**").uri("lb://order-service"))
                .build();
    }
}
