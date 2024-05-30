package com.desafioitau.api.transferencia.service.impl;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.desafioitau.api.transferencia.model.dto.NotificacaoRequestDTO;
import com.desafioitau.api.transferencia.service.impl.SQSServiceImpl;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SQSServiceImplTest {
    
    @InjectMocks
    private SQSServiceImpl sqsService;
    
    @Mock
    private AmazonSQS sqs;
    
    @Mock
    private Gson gson;
    
    
    
    private NotificacaoRequestDTO notificacaoRequestDTO;
    
    @BeforeEach
    public void setup() {
        notificacaoRequestDTO = NotificacaoRequestDTO.builder().build();
    }
    
    @Test
    public void testExecute() {
        SendMessageResult sendMessageResult = new SendMessageResult();
        when(sqs.sendMessage(any(SendMessageRequest.class))).thenReturn(sendMessageResult);
        
        Optional<Object> result = sqsService.execute(notificacaoRequestDTO);
        assertFalse(result.isPresent(), "Expected no result");
    }
    
    @Test
    public void testExecuteWhenExceptionThrown() {
        when(sqs.sendMessage(any(SendMessageRequest.class))).thenThrow(new RuntimeException("Failed to send message"));
        assertThrows(RuntimeException.class, () -> sqsService.execute(notificacaoRequestDTO), "Expected RuntimeException when sending message fails");
    }
    
    
    
    @Test
    public void testFallbackCircuitBreaker() {
        Exception exception = new Exception("Some exception");
        Optional<Object> result = sqsService.fallbackCircuitBreaker(notificacaoRequestDTO, exception);
        assertFalse(result.isPresent(), "Expected no result");
    }
}