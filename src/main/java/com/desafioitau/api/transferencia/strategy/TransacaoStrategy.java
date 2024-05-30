package com.desafioitau.api.transferencia.strategy;

import com.desafioitau.api.transferencia.model.entity.TipoTransacao;
import org.springframework.stereotype.Component;

@Component
public interface TransacaoStrategy {
    double atualizarSaldo(double saldo, double valorTransferencia);
    double atualizarLimiteDiario(double limiteDiario, double valorTransferencia);
    TipoTransacao getTipoTransacao();
}
