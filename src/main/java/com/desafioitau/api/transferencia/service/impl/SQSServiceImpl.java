package com.desafioitau.api.transferencia.service.impl;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.desafioitau.api.transferencia.model.dto.NotificacaoRequestDTO;
import com.desafioitau.api.transferencia.service.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SQSServiceImpl implements ApiService<Optional<Object>,NotificacaoRequestDTO> {
    
    private final AmazonSQS sqs;
    private final Gson gson;
    private final String queueUrl = "https://localhost.localstack.cloud:4566/000000000000/bacen_queue";
    
    @Autowired
    public SQSServiceImpl(AmazonSQS sqs, ObjectMapper objectMapper, Gson gson) {
        this.sqs = sqs;
        this.gson = gson;
    }
    
    @Override
    public Optional<Object> execute(NotificacaoRequestDTO notificacaoRequestDTO) {
        
        try {
            final String bacenRequest = gson.toJson(notificacaoRequestDTO);
            final SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(bacenRequest);
            sqs.sendMessage(sendMessageRequest);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao enviar a mensagem para a fila.", e);
        }
        
        return Optional.empty();
    }
    
    @Override
    public Optional<Object> fallbackRetry(NotificacaoRequestDTO notificacaoRequestDTO, Exception e) {
        return Optional.empty();
    }
    
    @Override
    public Optional<Object> fallbackCircuitBreaker(NotificacaoRequestDTO notificacaoRequestDTO, Exception e) {
        return Optional.empty();
    }
}
