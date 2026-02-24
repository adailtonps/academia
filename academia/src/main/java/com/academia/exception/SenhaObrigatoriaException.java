package com.academia.exception;

public class SenhaObrigatoriaException extends RuntimeException {
    public SenhaObrigatoriaException(String message) {
        super(message);
    }
}
