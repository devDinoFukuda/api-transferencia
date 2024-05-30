package com.desafioitau.api.transferencia.model.entity;

import lombok.*;

@Getter
 class Notificacao extends ContasTransacao {
    private double valor;
    
    Notificacao(String idOrigem, String idDestino) {
        super(idOrigem, idDestino);
    }
}
