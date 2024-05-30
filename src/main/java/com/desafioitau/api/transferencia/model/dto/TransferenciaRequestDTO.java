package com.desafioitau.api.transferencia.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaRequestDTO extends ContasTransacaoDTO {
    
    @NotNull(message = ConstantesBeanValidation.CAMPO_OBRIGATORIO)
    @Pattern(regexp = ConstantesBeanValidation.REGEX_PADRAO_ID,
        message = ConstantesBeanValidation.CAMPO_OBRIGATORIO)
    private String idCliente;
    
    @NotNull(message = ConstantesBeanValidation.CAMPO_OBRIGATORIO)
    @DecimalMin(value = ConstantesBeanValidation.VALOR_MINIMO,
        message = ConstantesBeanValidation.VALOR_INVALIDO)
    private double valor;
    
    @Builder
    public TransferenciaRequestDTO(String idOrigem, String idDestino, String idCliente, double valor) {
        super(idOrigem, idDestino);
        this.idCliente = idCliente;
        this.valor = valor;
    }
    
}
