package com.desafioitau.api.transferencia.model.entity;

import lombok.*;

@Getter
@Builder
class Saldo {
    private double valor;
    private String nomeDestino;
}
