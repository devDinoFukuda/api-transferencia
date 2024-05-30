package com.desafioitau.api.transferencia.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContaResponseDTO {
    private String id;
    private double saldo;
    private double limiteDiario;
    private boolean ativo;
}
