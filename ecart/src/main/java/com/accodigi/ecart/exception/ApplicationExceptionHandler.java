package com.accodigi.ecart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUserNotFoundException(UserNotFoundException e) {
        return Collections.singletonMap("errorMessage", e.getMessage());
    }

    @ExceptionHandler(PaymentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handlePaymentException(PaymentException e) {
        return Collections.singletonMap("errorMessage", e.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleProductNotFoundException(ProductNotFoundException e) {

        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", e.getMessage());
        return error;
    }

    @ExceptionHandler(EmptyCartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleEmptyCartException(EmptyCartException e) {

        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", e.getMessage());
        return error;
    }
}
