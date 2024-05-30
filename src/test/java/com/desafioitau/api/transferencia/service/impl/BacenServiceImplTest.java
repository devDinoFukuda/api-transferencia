package com.desafioitau.api.transferencia.service.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.desafioitau.api.transferencia.model.dto.NotificacaoRequestDTO;
import com.desafioitau.api.transferencia.model.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.model.entity.TransacaoEntity;
import com.desafioitau.api.transferencia.rest.BacenClientFeign;
import com.desafioitau.api.transferencia.service.ApiService;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BacenServiceImplTest {
    
    @InjectMocks
    private BacenServiceImpl service;
    
    @Mock
    private BacenClientFeign bacenClientFeign;
    
    @Mock
    private DynamoDBMapper  dynamoDB;
    
    @Mock
    private SQSServiceImpl sqs;
    
    @Mock
    ResponseEntity responseEntity;
    
    private UUID transactionUUID;
    private TransferenciaRequestDTO requestDTO;
    private Pair<TransferenciaRequestDTO, UUID> pair;
    
    @BeforeEach
    void setUp() {
        service = new BacenServiceImpl(bacenClientFeign, dynamoDB, sqs);
        transactionUUID = UUID.randomUUID();
        requestDTO = TransferenciaRequestDTO.builder().build();
        pair = Pair.with(requestDTO, transactionUUID);
    }
    
    @Test
    void deveRetonarFalhaDoEnviodaNotificacaoAoBacen() {
        UUID transactionUUID = UUID.randomUUID();
        TransferenciaRequestDTO requestDTO = TransferenciaRequestDTO.builder().build();
        
        when(bacenClientFeign.notificarBacen(any(NotificacaoRequestDTO.class))).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        Optional<ResponseEntity<?>> responseEntityOptional = service.execute(Pair.with(requestDTO, transactionUUID));
        assertTrue(responseEntityOptional.isPresent());
        ResponseEntity<?> responseEntity = responseEntityOptional.get();
        
        assertEquals(responseEntity.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        verify(bacenClientFeign, times(1)).notificarBacen(any(NotificacaoRequestDTO.class));
    }
    
    @Test
    void deveRetonarSucessoDoEnviodaNotificacaoAoBacen() {
        UUID transactionUUID = UUID.randomUUID();
        TransferenciaRequestDTO requestDTO = TransferenciaRequestDTO.builder().build();
        
        when(bacenClientFeign.notificarBacen(any(NotificacaoRequestDTO.class))).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));
        Optional<ResponseEntity<?>> responseEntityOptional = service.execute(Pair.with(requestDTO, transactionUUID));
        assertTrue(responseEntityOptional.isPresent());
        ResponseEntity<?> responseEntity = responseEntityOptional.get();
        
        assertEquals(responseEntity.getStatusCode(), HttpStatus.NO_CONTENT);
        verify(bacenClientFeign, times(1)).notificarBacen(any(NotificacaoRequestDTO.class));
    }
    
    @Test
    void shouldFallbackCircuitBreaker() {
        Exception exception = new RuntimeException();
        service.fallbackCircuitBreaker(pair, exception);
        verify(dynamoDB, times(1)).save(any(TransacaoEntity.class));
        verify(sqs, times(1)).execute(any(NotificacaoRequestDTO.class));
    }
    
    @Test
    void shouldThrowRuntimeExceptionInExecute() {
        when(bacenClientFeign.notificarBacen(any(NotificacaoRequestDTO.class))).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> service.execute(pair));
    }

}