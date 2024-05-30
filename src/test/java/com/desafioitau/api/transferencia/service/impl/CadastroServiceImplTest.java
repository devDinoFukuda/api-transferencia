package com.desafioitau.api.transferencia.service.impl;

import com.desafioitau.api.transferencia.errors.ConstantesException;
import com.desafioitau.api.transferencia.errors.exceptions.ApiCadastroUnavailableException;
import com.desafioitau.api.transferencia.errors.exceptions.ClienteException;
import com.desafioitau.api.transferencia.errors.exceptions.ClienteProcessamentoException;
import com.desafioitau.api.transferencia.model.dto.ClienteResponseDTO;
import com.desafioitau.api.transferencia.rest.CadastroClientFeign;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CadastroServiceImplTest {
    
    @InjectMocks
    private CadastroServiceImpl cadastroService;
    
    @Mock
    private CadastroClientFeign cadastroClientFeign;
    
    
    @Test
    void execute_shouldReturnFoundClient() {
        final String idCliente = UUID.randomUUID().toString();
        ClienteResponseDTO clientDTO = ClienteResponseDTO.builder()
            .id(idCliente)
            .build();
        
        when(cadastroClientFeign.consultarCliente(idCliente)).thenReturn(clientDTO);
        ClienteResponseDTO returnedClient = cadastroService.execute(idCliente);
        
        assertEquals(returnedClient, clientDTO);
        verify(cadastroClientFeign, times(1)).consultarCliente(idCliente);
    }
    
    @Test
    void testFallbackRetry() {
        String errorMessage = ConstantesException.CLIENTE_FALHA_API.getMensagem();
        Exception e = new Exception("Test Exception");
        
        ClienteProcessamentoException ex = assertThrows(
            ClienteProcessamentoException.class,
            () -> cadastroService.fallbackRetry("test", e)
        );
        
        assertEquals(errorMessage, ex.getMessage());
        assertEquals(HttpStatus.BAD_GATEWAY.value(), ex.getStatusCode());
    }
    
    @Test
    void testFallbackCircuitBreaker() {
        String errorMessage = ConstantesException.API_CADASTRO_INDISPONIVEL.getMensagem();
        
        Exception e = new Exception("Test Exception");
        
        ApiCadastroUnavailableException ex = assertThrows(
            ApiCadastroUnavailableException.class,
            () -> cadastroService.fallbackCircuitBreaker("test", e)
        );
        
        assertEquals(errorMessage, ex.getMessage());
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), ex.getStatusCode());
    }
}