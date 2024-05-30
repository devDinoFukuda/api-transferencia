package com.desafioitau.api.transferencia.model.entity;

import lombok.*;
import lombok.extern.log4j.Log4j2;

@Getter
@Builder
@Log4j2
class Cliente {
    private String id;
    private String nome;
    private String telefone;
    private String tipoPessoa;
}
