package com.desafioitau.api.transferencia.chain.conta;

import com.desafioitau.api.transferencia.chain.ContaValidator;
import com.desafioitau.api.transferencia.errors.exceptions.SaldoInsuficienteException;
import com.desafioitau.api.transferencia.model.entity.Conta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class ContaAtivaValidatorTest {
    
    private PossuiSaldoValidator contaAtivaValidator;
    private ContaValidator nextValidator;
    
    @BeforeEach
    void setUp() {
        nextValidator = mock(ContaValidator.class);
        contaAtivaValidator = new PossuiSaldoValidator();
        contaAtivaValidator.setNext(nextValidator);
    }
    
    @Test
    void deveValidarSaldoMenorQueValorTransferencia() {
        Conta conta = mock(Conta.class);
        when(conta.getSaldo()).thenReturn(500.00);
        double valorTransferencia = 600.00;
        Assertions.assertThrows(SaldoInsuficienteException.class, () -> contaAtivaValidator.validar(conta, valorTransferencia));
        verify(nextValidator, never()).validar(conta, valorTransferencia);
    }
    
    @Test
    void deveValidarQuandoSaldoMaiorQueValorTransferencia() {
        Conta conta = mock(Conta.class);
        when(conta.getSaldo()).thenReturn(600.00);
        double valorTransferencia = 500.00;
        contaAtivaValidator.validar(conta, valorTransferencia);
        verify(nextValidator, times(1)).validar(conta, valorTransferencia);
    }
    
}