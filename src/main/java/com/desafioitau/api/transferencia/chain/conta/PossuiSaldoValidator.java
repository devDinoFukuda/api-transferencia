package com.desafioitau.api.transferencia.chain.conta;

import com.desafioitau.api.transferencia.chain.ContaValidator;
import com.desafioitau.api.transferencia.errors.ConstantesException;
import com.desafioitau.api.transferencia.errors.exceptions.SaldoInsuficienteException;
import com.desafioitau.api.transferencia.model.entity.Conta;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class PossuiSaldoValidator implements ContaValidator {
    private ContaValidator next;
    @Override
    public void setNext(ContaValidator next) {
        this.next = next;
    }
    
    @Override
    public void validar(Conta conta, double valorTransferencia) {
        if((conta.getSaldo() - valorTransferencia) < 0) {
            log.error(ConstantesException.SALDO_INSUFICIENTE.getMensagem().concat(" Conta: {}"), conta.getId());
            throw new SaldoInsuficienteException(ConstantesException.SALDO_INSUFICIENTE.getMensagem());
        }
        log.info("Cliente : {} possui saldo na conta, transação em andamento", conta.getId());
        if (next != null) {
            next.validar(conta, valorTransferencia);
        }
    }
}
