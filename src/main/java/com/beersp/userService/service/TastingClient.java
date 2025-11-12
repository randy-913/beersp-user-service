package com.beersp.userService.service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatusCode;
import reactor.core.publisher.Mono;

import com.beersp.userService.model.Degustacion;
import com.beersp.userService.model.PageDegustacion;
import org.springframework.stereotype.Service;

@Service
public class TastingClient {
    private final WebClient webClient;

    public TastingClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8084/v1")
                .build();
    }

    public List<Degustacion> getDegustaciones(String idUsuario) {
        List<Degustacion> degustacionesList = new ArrayList<>();
        int page = 0;
        int size = 100;
        PageDegustacion degustaciones;
                
        do {
            degustaciones = webClient.get()
                .uri("/usuarios/{idUsuario}/degustaciones?page={page}&size={size}", idUsuario, page, size)
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
                .bodyToMono(PageDegustacion.class)
                .block();
            for(Degustacion degustacion : degustaciones.get_embedded().getDegustacionList()) {
                degustacionesList.add(degustacion);
            }

            page++;
        } while(degustaciones != null && degustaciones.getPage() != null && page < degustaciones.getPage().getTotalPages());
        
        return degustacionesList;                      
    }
}
