package com.desafioitau.api.transferencia.errors.exceptions;

import lombok.Getter;

@Getter
public class ClienteProcessamentoException extends RuntimeException{
    
    private final int statusCode;
    
    public ClienteProcessamentoException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
}
