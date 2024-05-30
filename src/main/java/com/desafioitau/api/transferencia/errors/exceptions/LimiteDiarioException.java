package com.desafioitau.api.transferencia.errors.exceptions;

public class LimiteDiarioException extends RuntimeException {
    public LimiteDiarioException(String message) {
        super(message);
    }
}
