package com.desafioitau.api.transferencia.service.impl;

import com.desafioitau.api.transferencia.errors.exceptions.TransferenciaProcessamentoException;
import com.desafioitau.api.transferencia.model.dto.ClienteResponseDTO;
import com.desafioitau.api.transferencia.model.dto.TransferenciaRequestDTO;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferenciaServiceImplTest {
    
    @InjectMocks
    private TransferenciaServiceImpl transferenciaService;
    @Mock
    private CadastroServiceImpl cadastroService;
    
    @Mock
    private ContaServiceImpl contaService;
    
    @Mock
    private BacenServiceImpl bacenService;
    
    @Test
    void deveExecutarOProcessamentoComSucesso() {
        final String idCliente = UUID.randomUUID().toString();
        final ClienteResponseDTO clienteResponseDTO = ClienteResponseDTO.builder().build();
        final TransferenciaRequestDTO transferenciaRequestDTO = TransferenciaRequestDTO.builder()
            .idCliente(idCliente)
            .build();
        
        when(cadastroService.execute(idCliente)).thenReturn(clienteResponseDTO);
        when(contaService.execute(Pair.with(clienteResponseDTO, transferenciaRequestDTO))).thenReturn(UUID.randomUUID());
        
        transferenciaService.execute(transferenciaRequestDTO);
        
        verify(cadastroService, times(1)).execute(idCliente);
        verify(contaService, times(1)).execute(Pair.with(clienteResponseDTO, transferenciaRequestDTO));
    }
    
    @Test
    void deveFalharOProcessamento() {
        TransferenciaRequestDTO requestDTO = null;
        
        assertThrows(TransferenciaProcessamentoException.class, () ->
            transferenciaService.execute(requestDTO));
    }
}