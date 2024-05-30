package com.desafioitau.api.transferencia.strategy.transacoes;

import com.desafioitau.api.transferencia.model.entity.TipoTransacao;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecebimentoStrategyTest {
    
    @Test
    public void deveAtualizarSaldo() {
        RecebimentoStrategy recebimentoStrategy = new RecebimentoStrategy();
        double saldo = 100.0;
        double valorTransferencia = 50.0;
        double expected = 150.0;
        
        double result = recebimentoStrategy.atualizarSaldo(saldo, valorTransferencia);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void deveAtualizarLimiteDiario() {
        RecebimentoStrategy recebimentoStrategy = new RecebimentoStrategy();
        double limiteDiario = 200.0;
        double valorTransferencia = 50.0;
        double expected = 200.0;
        
        double result = recebimentoStrategy.atualizarLimiteDiario(limiteDiario, valorTransferencia);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void deveTestarTipoTransacao() {
        RecebimentoStrategy recebimentoStrategy = new RecebimentoStrategy();
        TipoTransacao expected= TipoTransacao.RECEBIMENTO;
        
        TipoTransacao result = recebimentoStrategy.getTipoTransacao();
        
        assertEquals(expected, result);
    }
    
}