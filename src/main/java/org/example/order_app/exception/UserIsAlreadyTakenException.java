package org.example.order_app.exception;

public class UserIsAlreadyTakenException extends RuntimeException {
    public UserIsAlreadyTakenException(String message) {
        super(message);
    }
}
