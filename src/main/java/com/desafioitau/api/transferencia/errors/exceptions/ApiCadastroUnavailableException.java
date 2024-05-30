package com.desafioitau.api.transferencia.errors.exceptions;

import lombok.Getter;

@Getter
public class ApiCadastroUnavailableException extends RuntimeException{
    
    private final int statusCode;
    
    public ApiCadastroUnavailableException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
}
