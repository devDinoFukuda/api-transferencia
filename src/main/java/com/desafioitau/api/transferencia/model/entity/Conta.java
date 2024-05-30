package com.desafioitau.api.transferencia.model.entity;

import com.desafioitau.api.transferencia.chain.ContaValidator;
import com.desafioitau.api.transferencia.strategy.TransacaoStrategy;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Getter
@Log4j2
public class Conta extends  IdConta {
    private double saldo;
    private double limiteDiario;
    private boolean ativo;
    
    protected Conta(String idConta,  double saldo,  double limiteDiario,  boolean ativo,  double valorTransferencia,  ContaValidator contaValidator) {
        super(idConta);
        this.saldo = saldo;
        this.limiteDiario = limiteDiario;
        this.ativo = ativo;
        contaValidator.validar(this, valorTransferencia);
    }
    
   protected Conta(String idConta,  double saldo,  double limiteDiario,  boolean ativo, double valorTransferencia, TransacaoStrategy transacaoStrategy) {
        super(idConta);
        this.saldo = transacaoStrategy.atualizarSaldo(saldo, valorTransferencia);
        this.limiteDiario = transacaoStrategy.atualizarLimiteDiario(limiteDiario, valorTransferencia);
        this.ativo = ativo;
    }
    
}
