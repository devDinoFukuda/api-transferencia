package com.desafioitau.api.transferencia.model.entity;

import lombok.*;

@Getter
@Builder
class ContasTransacao {
    private String idOrigem;
    private String idDestino;
}
