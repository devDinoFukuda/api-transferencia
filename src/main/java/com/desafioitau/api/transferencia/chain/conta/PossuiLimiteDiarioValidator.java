package com.desafioitau.api.transferencia.chain.conta;

import com.desafioitau.api.transferencia.chain.ContaValidator;
import com.desafioitau.api.transferencia.errors.ConstantesException;
import com.desafioitau.api.transferencia.errors.exceptions.LimiteDiarioException;
import com.desafioitau.api.transferencia.model.entity.Conta;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class PossuiLimiteDiarioValidator implements ContaValidator {
    private ContaValidator next;
    @Override
    public void setNext(ContaValidator next) {
        this.next = next;
    }
    
    @Override
    public void validar(Conta conta, double valorTransferencia) {
        if (conta.getLimiteDiario() == 0) {
            throw new LimiteDiarioException(ConstantesException.LIMITE_DIARIO_INEXISTENTE.getMensagem());
        }
        if ((conta.getLimiteDiario() - valorTransferencia) < 0) {
            log.error(ConstantesException.LIMITE_DIARIO_EXCEDIDO.getMensagem().concat(" Conta: {}"), conta.getId());
            throw new LimiteDiarioException(ConstantesException.LIMITE_DIARIO_EXCEDIDO.getMensagem());
        }
        log.info("Cliente: {} possui limite diário, transação em andamento", conta.getId());
    
        if (next != null) {
            next.validar(conta, valorTransferencia);
        }
    }
}
