package com.hana.client_service.exception;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(Long id) {
        super("Client with ID " + id + " not found.");
    }

    public ClientNotFoundException(String message) {
        super(message);
    }
}