package com.desafioitau.api.transferencia.chain.conta;

import com.desafioitau.api.transferencia.chain.ContaValidator;
import com.desafioitau.api.transferencia.errors.ConstantesException;
import com.desafioitau.api.transferencia.errors.exceptions.ContaInativaException;
import com.desafioitau.api.transferencia.model.entity.Conta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PossuiSaldoValidatorTest {
    
    private ContaAtivaValidator validator;
    private ContaValidator nextValidator;
    private Conta conta;
    
    @BeforeEach
    void setUp() {
        validator = new ContaAtivaValidator();
        nextValidator = mock(ContaValidator.class);
        conta = mock(Conta.class);
        
        validator.setNext(nextValidator);
    }
    
    @Test
    void deveValidarContaInativa() {
        when(conta.isAtivo()).thenReturn(false);
        
        Exception exception = assertThrows(ContaInativaException.class, () ->
            validator.validar(conta, 100.0));
        
        assertEquals(ConstantesException.CONTA_INATIVA.getMensagem(),
            exception.getMessage());
    }
    
    @Test
    void deveValidarContaAtiva() {
        when(conta.isAtivo()).thenReturn(true);
        
        assertDoesNotThrow(() -> validator.validar(conta, 100.0));
        
        verify(nextValidator, times(1)).validar(conta, 100.0);
    }

}