package com.desafioitau.api.transferencia.service.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.desafioitau.api.transferencia.model.dto.NotificacaoRequestDTO;
import com.desafioitau.api.transferencia.model.dto.PropertiesDTO;
import com.desafioitau.api.transferencia.model.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.model.entity.BacenNotificado;
import com.desafioitau.api.transferencia.model.entity.TransacaoEntity;
import com.desafioitau.api.transferencia.rest.BacenClientFeign;
import com.desafioitau.api.transferencia.service.ApiService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.log4j.Log4j2;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public class BacenServiceImpl implements ApiService<Optional<ResponseEntity<?>>, Pair<TransferenciaRequestDTO, UUID>> {
    
    private  final BacenClientFeign bacenClientFeign;
    private final DynamoDBMapper dynamoDB;
    private  final ApiService sqs;
    
    @Autowired
    public BacenServiceImpl (BacenClientFeign bacenClientFeign, DynamoDBMapper dynamoDB, SQSServiceImpl sqs ) {
        this.bacenClientFeign = bacenClientFeign;
        this.dynamoDB = dynamoDB;
        this.sqs = sqs;
    }
    
    @CircuitBreaker(name = "backendA", fallbackMethod = "fallbackCircuitBreaker")
    @Override
    public Optional<ResponseEntity<?>> execute(Pair<TransferenciaRequestDTO, UUID> dadosBacen) {
        log.info("Enviando notificação ao bacen da transação efetuada");
        return Optional.ofNullable(dadosBacen)
            .map(dados -> {
                final TransferenciaRequestDTO transferenciaRequestDTO = dados.getValue0();
                final UUID idTransacao = dados.getValue1();
                return NotificacaoRequestDTO.builder()
                    .valor(transferenciaRequestDTO.getValor())
                    .idTransacao(idTransacao)
                    .idOrigem(transferenciaRequestDTO.getIdOrigem())
                    .idDestino(transferenciaRequestDTO.getIdDestino())
                    .build();
            })
            .map(bacenClientFeign::notificarBacen)
            .map(bacenResponse -> {
                if (!bacenResponse.getStatusCode().is2xxSuccessful()) {
                    var transferenciaEntity = this.getTransacaoEntity(dadosBacen.getValue0(), dadosBacen.getValue1(), BacenNotificado.N);
                    dynamoDB.save(transferenciaEntity);
                    log.info("Dados salvos na base de transferências com falha no envio da notificação ao bacen: idTransacao: {}",transferenciaEntity.getIdTransacao()  );
                    
                    sqs.execute(this.getNotificacaoRequestDTO(transferenciaEntity));
                    log.warn("Erro ao notificar o bacen da transação efetuada, dados enviados a fila SQS para tratativa: idTransacao: {}", transferenciaEntity.getIdTransacao());
                } else {
                    log.info("Notificação da transação: {} ao bacen efetuada com sucesso", dadosBacen.getValue1());;
                    var transferenciaEntity =  this.getTransacaoEntity(dadosBacen.getValue0(), dadosBacen.getValue1(), BacenNotificado.S);
                    dynamoDB.save(transferenciaEntity);
                    log.info("Dados da transferência inserido na base de transferências: idTransacao: {}",transferenciaEntity.getIdTransacao()  );
                }
                return bacenResponse;
            });
    }
    
    @Override
    public Optional<ResponseEntity<?>> fallbackRetry(Pair<TransferenciaRequestDTO, UUID> dadosBacen, Exception e) {
        return Optional.empty();
    }
    
    @Override
    public Optional<ResponseEntity<?>> fallbackCircuitBreaker(Pair<TransferenciaRequestDTO, UUID> dadosBacen, Exception e) {
        var transferenciaEntity =  this.getTransacaoEntity(dadosBacen.getValue0(), dadosBacen.getValue1(), BacenNotificado.N);
        dynamoDB.save(transferenciaEntity);
        sqs.execute(this.getNotificacaoRequestDTO(transferenciaEntity));
        log.warn("Falha na comunicação com API do Bacen, , dados da transação enviados a fila SQS para tratativa: idTransacao: {}", transferenciaEntity.getIdTransacao());
        return Optional.empty();
    }
    
    private TransacaoEntity getTransacaoEntity(TransferenciaRequestDTO transferenciaRequestDTO, UUID idTransacao, BacenNotificado bacenNotificado) {
        return  TransacaoEntity.builder()
            .idTransacao(idTransacao)
            .idContaOrigem(transferenciaRequestDTO.getIdOrigem())
            .idContaDestino(transferenciaRequestDTO.getIdDestino())
            .valorTransferencia(transferenciaRequestDTO.getValor())
            .bacenNotificado(bacenNotificado.getMensagem())
            .build();
    }
    
    private NotificacaoRequestDTO getNotificacaoRequestDTO(TransacaoEntity transacaoEntity) {
        return  NotificacaoRequestDTO.builder()
            .idTransacao(transacaoEntity.getIdTransacao())
            .valor(transacaoEntity.getValorTransferencia())
            .idOrigem(transacaoEntity.getIdContaOrigem())
            .idDestino(transacaoEntity.getIdContaDestino()).build();
    }
    
}
