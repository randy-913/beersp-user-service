package com.beersp.userService.service;

import org.springframework.web.reactive.function.client.WebClient;


import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import com.beersp.userService.model.Cerveza;
//import com.beersp.userService.model.PageCerveza;

@Service
public class BeerClient {
    private final WebClient webClient;

    public BeerClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8083/v1")
                .build();
    }

    public Cerveza getCervezaById(String idCerveza) {
        return webClient.get()
                .uri("/cervezas/{idCerveza}", idCerveza)
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
                .bodyToMono(Cerveza.class)
                .block();
    }
}
