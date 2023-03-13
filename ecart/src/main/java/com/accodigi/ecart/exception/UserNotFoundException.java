package com.accodigi.ecart.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("There is no user exists with ID " + userId);
    }
}
