package com.desafioitau.api.transferencia.service.impl;

import com.desafioitau.api.transferencia.errors.exceptions.ApiContasUnavailableException;
import com.desafioitau.api.transferencia.errors.exceptions.ContaProcessamentoException;
import com.desafioitau.api.transferencia.model.dto.ClienteResponseDTO;
import com.desafioitau.api.transferencia.model.dto.ContaRequestDTO;
import com.desafioitau.api.transferencia.model.dto.ContaResponseDTO;
import com.desafioitau.api.transferencia.model.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.model.entity.ContaFacade;
import com.desafioitau.api.transferencia.rest.ContasClientFeign;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContaServiceImplTest {
    
    @InjectMocks
    private ContaServiceImpl contaService;
    @Mock
    private ContasClientFeign contasClientFeign;
    @Mock
    private ContaFacade contaFacade;
    
    private TransferenciaRequestDTO requestDTO;
    private String idCliente, idOrigem, idDestino;
    private ClienteResponseDTO cliente;
    private ContaResponseDTO conta;
    Pair<ClienteResponseDTO, TransferenciaRequestDTO> pair ;
    
    @BeforeEach
    void setup() {
        idOrigem = UUID.randomUUID().toString();
        idDestino = UUID.randomUUID().toString();
        idCliente = UUID.randomUUID().toString();
        
        requestDTO = TransferenciaRequestDTO.builder()
            .idDestino(idDestino)
            .idOrigem(idOrigem)
            .idCliente(idCliente)
            .valor(100.0)
            .build();
        
        cliente = ClienteResponseDTO.builder()
            .id(idCliente)
            .nome("Teste")
            .tipoPessoa("PF")
            .telefone("11999999999")
            .build();
        
        conta = ContaResponseDTO.builder()
            .id(idOrigem)
            .ativo(true)
            .saldo(1000.0)
            .limiteDiario(1000.0)
            .build();
        
        pair = Pair.with(cliente, requestDTO);
    }
    
    @Test
    public void deveProcessarComSucessoOFluxo() {
        
        when(contasClientFeign.consultaContaPorIdConta(anyString())).thenReturn(conta);
        when(contasClientFeign.atualizaSaldo(any())).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));
        when(contaFacade.validarConta(any(), anyDouble())).thenReturn(conta);
        when(contaFacade.executarTransferencia(any()))
            .thenReturn(new Pair<>(ContaRequestDTO.builder().build(), ContaRequestDTO.builder().build()));
      
        UUID result = contaService.execute(pair);
        
        verifyMocks();
        assertNotNull(result);
    }
    
    @Test
    public void deveProcessarFluxoComFalhanaAtualizacao() {
        
        when(contasClientFeign.consultaContaPorIdConta(anyString())).thenReturn(conta);
        when(contasClientFeign.atualizaSaldo(any())).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        when(contaFacade.validarConta(any(), anyDouble())).thenReturn(conta);
        when(contaFacade.executarTransferencia(any()))
            .thenReturn(new Pair<>(ContaRequestDTO.builder().build(), ContaRequestDTO.builder().build()));
        
        UUID result = contaService.execute(pair);
        
        verifyMocks();
        assertNotNull(result);
    }
    
    @Test
    public void tdeveRetornarErroNoProcessamento() {
       
        ClienteResponseDTO cliente = ClienteResponseDTO.builder().build();
        TransferenciaRequestDTO transferencia = TransferenciaRequestDTO.builder().build();
        Pair<ClienteResponseDTO, TransferenciaRequestDTO> pair = new Pair<>(cliente, transferencia);
        
        assertThrows(ContaProcessamentoException.class, () -> {
            contaService.execute(pair);
        });
    }
    
    
    @Test
    @DisplayName("Test fallbackRetry method")
    void testFallbackRetry() {
        Exception mockException = new Exception("This is a test exception");
        Pair<ClienteResponseDTO, TransferenciaRequestDTO> pair = null; //Você precisa instanciar o par aqui, dependendo de seus objetos.
        
        ContaProcessamentoException exception =
            assertThrows(ContaProcessamentoException.class, () -> {
                contaService.fallbackRetry(pair, mockException);
            });
        
        assertEquals(HttpStatus.BAD_GATEWAY.value(), exception.getStatusCode());
        assertEquals("Falha no processamento", exception.getMessage());
        // Aqui também você pode verificar se o log.warn foi chamado com a mensagem e exceção corretas.
    }
    
    @Test
    @DisplayName("Test fallbackCircuitBreaker method")
    void testFallbackCircuitBreaker() {
        Exception mockException = new Exception("This is a test exception");
        Pair<ClienteResponseDTO, TransferenciaRequestDTO> pair = new Pair<>(cliente, TransferenciaRequestDTO.builder().build());
        
        ApiContasUnavailableException exception =
            assertThrows(ApiContasUnavailableException.class, () -> {
               contaService.fallbackCircuitBreaker(pair, mockException);
            });
        
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), exception.getStatusCode());
        assertEquals("Api Conta indisponivel", exception.getMessage());
        // Aqui também você pode verificar se o log.warn foi chamado com a mensagem e exceção corretas.
    }
    
    private void verifyMocks() {
        verify(contasClientFeign, times(2)).consultaContaPorIdConta(anyString());
        verify(contasClientFeign, times(2)).atualizaSaldo(any());
        verify(contaFacade, times(1)).validarConta(any(), anyDouble());
        verify(contaFacade, times(1)).executarTransferencia(any());
    }

}