package com.desafioitau.api.transferencia.model.entity;

import lombok.*;

@Getter
@Builder
class IdConta {
    private String id;
    
    protected IdConta(String id) {
        this.id = id;
    }
    
}
