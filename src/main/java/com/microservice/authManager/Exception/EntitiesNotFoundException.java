package com.microservice.authManager.Exception;

public class EntitiesNotFoundException extends RuntimeException {
    public EntitiesNotFoundException(String message) {
        super(message);
    }
}
