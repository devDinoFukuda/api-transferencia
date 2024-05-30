package com.desafioitau.api.transferencia.errors.exceptions;

import lombok.Getter;

@Getter
public class ContaProcessamentoException extends RuntimeException {
    private final int statusCode;
    
    public ContaProcessamentoException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
