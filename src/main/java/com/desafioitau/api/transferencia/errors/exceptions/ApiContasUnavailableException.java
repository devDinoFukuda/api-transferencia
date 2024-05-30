package com.desafioitau.api.transferencia.errors.exceptions;

import lombok.Getter;

@Getter
public class ApiContasUnavailableException extends RuntimeException{
    
    private final int statusCode;
    
    public ApiContasUnavailableException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
}
