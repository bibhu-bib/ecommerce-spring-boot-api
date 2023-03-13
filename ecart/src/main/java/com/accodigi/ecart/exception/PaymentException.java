package com.accodigi.ecart.exception;

public abstract class PaymentException extends RuntimeException {

    public PaymentException(String message) {
        super(message);
    }
}
