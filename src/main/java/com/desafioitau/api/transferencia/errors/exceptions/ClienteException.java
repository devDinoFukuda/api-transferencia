package com.desafioitau.api.transferencia.errors.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ClienteException extends RuntimeException{
    private final int statusCode;
    
    public ClienteException(String message,  Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
}
