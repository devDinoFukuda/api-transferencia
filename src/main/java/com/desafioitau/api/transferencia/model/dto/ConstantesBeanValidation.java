package com.desafioitau.api.transferencia.model.dto;

class ConstantesBeanValidation {
    
    static final String CAMPO_OBRIGATORIO = "Campo obrigatório";
    static final String PADRAO_ID_INVALIDO = "Padrão inválido";
    static final String  REGEX_PADRAO_ID = "^[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}$";
    static final String VALOR_INVALIDO = "Valor inválido";
    static final String VALOR_MINIMO = "0.001";
}
