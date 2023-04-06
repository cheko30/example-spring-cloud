package com.spo.hotel.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("Resource not found in the server!!!");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
