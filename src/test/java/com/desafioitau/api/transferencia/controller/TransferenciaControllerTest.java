package com.desafioitau.api.transferencia.controller;

import com.desafioitau.api.transferencia.model.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.model.dto.TransferenciaResponseDTO;
import com.desafioitau.api.transferencia.service.impl.BacenServiceImpl;
import com.desafioitau.api.transferencia.service.impl.TransferenciaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferenciaControllerTest {
    
    @InjectMocks
    private TransferenciaController controller;
    
    @Mock
    private TransferenciaServiceImpl transferenciaService;
    
    @Mock
    private BacenServiceImpl bacenService;
    
    private MockHttpServletRequest  request;
    
    @BeforeEach
    void setUp() {
        controller = new TransferenciaController(transferenciaService,  bacenService);
        request = new MockHttpServletRequest();
        request.setRequestURI("/transferencia");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }
    
    @Test
    void deveEfetuarTransferenciaComSucesso() {
        UUID idTransferencia = UUID.randomUUID();
        when(transferenciaService.execute(any())).thenReturn(idTransferencia);
        
        TransferenciaRequestDTO request = TransferenciaRequestDTO.builder().build();
        ResponseEntity<TransferenciaResponseDTO> response = controller.efetuarTransferencia(request);
        
        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getHeaders().getLocation());
        assertEquals(idTransferencia, response.getBody().getIdTransferencia());
        
        String expectedUri = "/transferencia/" + idTransferencia;
        String actualUri = response.getHeaders().getLocation().getPath();
        assertEquals(expectedUri, actualUri);
        
    }
    
}