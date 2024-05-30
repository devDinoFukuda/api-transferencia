package com.desafioitau.api.transferencia.strategy.transacoes;

import com.desafioitau.api.transferencia.model.entity.TipoTransacao;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PagamentoStrategyTest {
    
    @Test
    void deveAtualizarSaldo() {
        PagamentoStrategy pagamentoStrategy = new PagamentoStrategy();
        double saldo = 500.0;
        double valorTransferencia = 200.0;
        double expected = 300.0;
        
        double result = pagamentoStrategy.atualizarSaldo(saldo, valorTransferencia);
        
        assertEquals(expected, result);
    }
    
    @Test
    void deveAtualizarLimiteDiario() {
        PagamentoStrategy pagamentoStrategy = new PagamentoStrategy();
        double limiteDiario = 500.0;
        double valorTransferencia = 200.0;
        double expected = 300.0;
        
        double result = pagamentoStrategy.atualizarLimiteDiario(limiteDiario, valorTransferencia);
        
        assertEquals(expected, result);
    }
    
    @Test
    void deveTestarTipoTransacao() {
        PagamentoStrategy pagamentoStrategy = new PagamentoStrategy();
        TipoTransacao expected = TipoTransacao.PAGAMENTO;
        
        TipoTransacao result = pagamentoStrategy.getTipoTransacao();
        
        assertEquals(expected, result);
    }
    
}