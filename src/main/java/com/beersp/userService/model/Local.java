package com.beersp.userService.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Local {
    String idLocal;
    String nombre;
    String direccion;
    private ResourceLink _links;
}
