package com.academia.exception;

public class UserNaoEncontradoException extends RuntimeException {
    public UserNaoEncontradoException(String message) {
        super(message);
    }
}
