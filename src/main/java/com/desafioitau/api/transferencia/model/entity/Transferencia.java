package com.desafioitau.api.transferencia.model.entity;

import lombok.*;

@Getter
class Transferencia extends ContasTransacao {
    private String idCliente;
    private double valor;
    
    Transferencia(String idOrigem, String idDestino) {
        super(idOrigem, idDestino);
    }
}
