package com.example.microservice.orderservice.webclient;

import com.example.microservice.orderservice.dto.UserDto;
import com.example.microservice.orderservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserWebClientService {
    private final WebClient.Builder webClientBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserDto getUserById(Long userId) {
        return webClientBuilder.build()
                .get()
                .uri("http://product-service/users/user/{id}", userId)
                .header("Authorization", "Bearer " + jwtTokenProvider.getToken())
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }

}
