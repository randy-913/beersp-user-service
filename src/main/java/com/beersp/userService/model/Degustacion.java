package com.beersp.userService.model;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Degustacion {
    private String idDegustacion;
    private String idUsuario;
    private String idCerveza;
    private String idLocal;
    private LocalDate fechaDegustacion;
    private int puntuacion;
    private ResourceLink _links;
}
