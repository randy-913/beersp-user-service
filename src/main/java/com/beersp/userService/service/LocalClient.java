package com.beersp.userService.service;

import org.springframework.web.reactive.function.client.WebClient;


import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import com.beersp.userService.model.Local;

@Service
public class LocalClient {
    private final WebClient webClient;

    public LocalClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8085/v1")
                .build();
    }

    public Local getLocalById(String idLocal) {
        return webClient.get()
                .uri("/locales/{idLocal}", idLocal)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> response
                                .bodyToMono(String.class)
                                .doOnNext(body -> System.err.println("Error 4xx: " + body))
                                .then(Mono.empty())
                )
                .onStatus(HttpStatusCode::is5xxServerError, response -> response
                                .bodyToMono(String.class)
                                .doOnNext(body -> System.err.println("Error 5xx: " + body))
                                .then(Mono.empty()))
                .bodyToMono(Local.class)
                .block();
    }
}
