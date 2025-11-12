package com.beersp.userService.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageLocal {
    private Locales _embedded;
    private PageLinks _links;
    private PageMetadata page;
}
