package com.beersp.userService.exception;

public class IllegalAgeException extends RuntimeException {
    public IllegalAgeException(String message) {
        super(message);
    }
}
