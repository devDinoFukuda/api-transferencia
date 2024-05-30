package com.desafioitau.api.transferencia.errors;

import lombok.Getter;

import java.io.Serial;
import java.time.LocalDateTime;

@Getter
public class ValidatorErro extends TratamentoErro {
    
    @Serial
    private static final long serialVersionUID = 6397022481770795438L;
   
    public ValidatorErro(LocalDateTime timestamp, String path, Integer status, String error, String message) {
        super(timestamp, path, status, error, message);
    }
    
}
