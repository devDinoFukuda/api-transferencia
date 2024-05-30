package com.desafioitau.api.transferencia.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BacenNotificado {
    S("Sim"), N("Nao");
    
    private String mensagem;
}
