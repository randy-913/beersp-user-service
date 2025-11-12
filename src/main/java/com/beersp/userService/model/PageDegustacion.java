package com.beersp.userService.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageDegustacion {
    private Degustaciones _embedded;
    private PageLinks _links;
    private PageMetadata page;
}
