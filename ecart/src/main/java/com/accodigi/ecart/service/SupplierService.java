package com.accodigi.ecart.service;

import com.accodigi.ecart.model.order.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class SupplierService {
    private final WebClient webClient;
    @Value("${supplier.api.endpoint}")
    private final String endpoint;

    public SupplierService(@Value("${supplier.api.baseUrl}") String baseUrl,
                           @Value("${supplier.api.endpoint}") String endpoint) {
        this.endpoint = endpoint;
        webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<Order> notifySupplier(Order order) {
        return webClient.post()
                .uri(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(order), Order.class)
                .retrieve()
                .bodyToMono(Order.class);
    }
}
