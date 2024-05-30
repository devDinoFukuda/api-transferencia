package com.desafioitau.api.transferencia.chain.conta;

import com.desafioitau.api.transferencia.chain.ContaValidator;
import com.desafioitau.api.transferencia.errors.ConstantesException;
import com.desafioitau.api.transferencia.errors.exceptions.LimiteDiarioException;
import com.desafioitau.api.transferencia.model.entity.Conta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PossuiLimiteDiarioValidatorTest {
    
    private PossuiLimiteDiarioValidator validator;
    private ContaValidator nextValidator;
    private Conta conta;
    
    @BeforeEach
    void setUp() {
        validator = new PossuiLimiteDiarioValidator();
        nextValidator = mock(ContaValidator.class);
        conta = mock(Conta.class);
        
        validator.setNext(nextValidator);
    }
    
    @Test
    void deveValidarContaSemLimiteDiario() {
        when(conta.getLimiteDiario()).thenReturn(0.0);
        
        Exception exception = assertThrows(LimiteDiarioException.class, () ->
            validator.validar(conta, 100.0));
        
        assertEquals(ConstantesException.LIMITE_DIARIO_INEXISTENTE.getMensagem(),
            exception.getMessage());
    }
    
    @Test
    void deveValidarContaExcedeLimiteDiario() {
        when(conta.getLimiteDiario()).thenReturn(50.0);
        
        Exception exception = assertThrows(LimiteDiarioException.class, () ->
            validator.validar(conta, 100.0));
        
        assertEquals(ConstantesException.LIMITE_DIARIO_EXCEDIDO.getMensagem(),
            exception.getMessage());
    }
    
    @Test
    void deveValidarContaDentroDoLimiteDiario() {
        when(conta.getLimiteDiario()).thenReturn(200.0);
        
        assertDoesNotThrow(() -> validator.validar(conta, 100.0));
        
        verify(nextValidator, times(1)).validar(conta, 100.0);
    }

}