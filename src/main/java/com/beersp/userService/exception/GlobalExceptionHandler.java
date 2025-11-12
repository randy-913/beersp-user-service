package com.beersp.userService.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }
    
    @ExceptionHandler(DuplicateUserException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleDuplicateUserException(DuplicateUserException ex) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(IllegalAgeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleIllegalAgeException(IllegalAgeException ex) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(NotFoundUserException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFoundUserException(NotFoundUserException ex) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(UserNotVerifiedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage handleUserNotVerifiedException(UserNotVerifiedException ex) {
        return new ErrorMessage(ex.getMessage());
    }

}
