package com.desafioitau.api.transferencia.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContasTransacaoDTO {
    
    @NotNull(message = ConstantesBeanValidation.CAMPO_OBRIGATORIO)
    @Pattern(regexp = ConstantesBeanValidation.REGEX_PADRAO_ID,
        message = ConstantesBeanValidation.PADRAO_ID_INVALIDO)
    
    private String idOrigem;
    @NotNull(message = ConstantesBeanValidation.CAMPO_OBRIGATORIO)
    @Pattern(regexp = ConstantesBeanValidation.REGEX_PADRAO_ID,
        message = ConstantesBeanValidation.PADRAO_ID_INVALIDO)
    private String idDestino;
}
