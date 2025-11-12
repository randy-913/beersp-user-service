package com.beersp.userService.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageCerveza {
    private Cervezas _embedded;
    private PageLinks _links;
    private PageMetadata page;
}
