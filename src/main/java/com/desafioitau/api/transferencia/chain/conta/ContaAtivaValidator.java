package com.desafioitau.api.transferencia.chain.conta;

import com.desafioitau.api.transferencia.chain.ContaValidator;
import com.desafioitau.api.transferencia.errors.ConstantesException;
import com.desafioitau.api.transferencia.errors.exceptions.ContaInativaException;
import com.desafioitau.api.transferencia.model.entity.Conta;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ContaAtivaValidator implements ContaValidator {
    private ContaValidator next;
    @Override
    public void setNext(ContaValidator next) {
        this.next = next;
    }
    
    @Override
    public void validar(Conta conta, double valorTransferencia) {
        if(!conta.isAtivo()) {
            log.error(ConstantesException.CONTA_INATIVA.getMensagem().concat(" Conta: {}"), conta.getId());
            throw new ContaInativaException(ConstantesException.CONTA_INATIVA.getMensagem());
        }
        log.info("Conta: {} está ativa, transação em andamento.", conta.getId());
        if (next != null) {
            next.validar(conta, valorTransferencia);
        }
    }
}
