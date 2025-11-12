package com.beersp.userService.model;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cerveza {
    String idCerveza;
    String nombre;
    String estilo;
    String paisProcedencia;
    String tamaño;
    String formato;
    String porcentajeAlcohol;
    String calificadorAmargor;
    String color;
    String foto;
    LocalDate fechaAlta;
    private ResourceLink _links;
}
