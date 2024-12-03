package com.example.letscook.Exceptions;

public class InvalidServingException extends RuntimeException {
    public InvalidServingException(String message) {
        super(message);
    }
}
