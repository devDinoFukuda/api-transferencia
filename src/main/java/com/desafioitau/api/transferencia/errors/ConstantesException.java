package com.desafioitau.api.transferencia.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConstantesException {
    CONTA_INATIVA("Conta está inativa, não sendo possivel seguir com a transação"),
    SALDO_INSUFICIENTE("Saldo insuficiente para prosseguir com a transação"),
    LIMITE_DIARIO_INEXISTENTE("Cliente não possui limite diário para esta transação"),
    LIMITE_DIARIO_EXCEDIDO("Não é possivel concluir a transação, valor solicitado ultrapassa o limite diário"),
    CLIENTE_NAO_ENCONTRADO("Cliente não encontrado"),
    CLIENTE_FALHA_API("Falha Api de Cadastro"),
    CONTA_FALHA_PROCESSAMENTO("Falha  no processamento do fluxo de contas, não sendo possivel seguir com a transação"),
    TRANSFERENCIA_FALHA_PROCESSAMENTO("Falha no processamento do fluxo de transferência, não sendo possivel seguir com a transação"),
    API_CONTA_INDISPONIVEL("Api Conta indisponivel"),
    API_CADASTRO_INDISPONIVEL("Api de Cadastro indisponivel")
    ;
    
   private String mensagem;
    
}
