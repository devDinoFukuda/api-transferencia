package com.desafioitau.api.transferencia.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificacaoRequestDTO extends ContasTransacaoDTO {
    private double valor;
    private UUID idTransacao;
    
    @Builder
    public NotificacaoRequestDTO(String idOrigem, String idDestino, double valor, UUID idTransacao) {
        super(idOrigem, idDestino);
        this.valor = valor;
        this.idTransacao = idTransacao;
    }
}
