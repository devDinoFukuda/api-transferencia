package com.desafioitau.api.transferencia.strategy.transacoes;

import com.desafioitau.api.transferencia.model.entity.TipoTransacao;
import com.desafioitau.api.transferencia.strategy.TransacaoStrategy;
import org.springframework.stereotype.Component;

@Component
public class RecebimentoStrategy implements TransacaoStrategy {
    @Override
    public double atualizarSaldo(double saldo, double valorTransferencia) {
        return saldo + valorTransferencia;
    }
    
    @Override
    public double atualizarLimiteDiario(double limiteDiario, double valorTransferencia) {
        return limiteDiario;
    }
    
    @Override
    public TipoTransacao getTipoTransacao() {
        return TipoTransacao.RECEBIMENTO;
    }
}
