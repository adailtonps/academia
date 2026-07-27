package com.academia.exception;

public class UserNaoPodeApagarException extends RuntimeException {
    public UserNaoPodeApagarException(String message) {
        super(message);
    }
}
