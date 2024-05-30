package com.desafioitau.api.transferencia.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponseDTO {
    private String id;
    private String nome;
    private String telefone;
    private String tipoPessoa;
}
