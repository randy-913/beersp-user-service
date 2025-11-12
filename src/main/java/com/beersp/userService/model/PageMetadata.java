package com.beersp.userService.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageMetadata {
    private int size;
    private int totalElements;
    private int totalPages;
    private int number;
}

