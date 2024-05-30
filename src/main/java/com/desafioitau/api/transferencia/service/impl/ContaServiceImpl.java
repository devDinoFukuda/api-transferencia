package com.desafioitau.api.transferencia.service.impl;

import com.desafioitau.api.transferencia.errors.ConstantesException;
import com.desafioitau.api.transferencia.errors.exceptions.ApiContasUnavailableException;
import com.desafioitau.api.transferencia.errors.exceptions.ContaProcessamentoException;
import com.desafioitau.api.transferencia.model.dto.ClienteResponseDTO;
import com.desafioitau.api.transferencia.model.dto.ContaResponseDTO;
import com.desafioitau.api.transferencia.model.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.model.entity.ContaFacade;
import com.desafioitau.api.transferencia.rest.ContasClientFeign;
import com.desafioitau.api.transferencia.service.ApiService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.log4j.Log4j2;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public class ContaServiceImpl implements ApiService<UUID, Pair< ClienteResponseDTO, TransferenciaRequestDTO>> {
    
    private  final ContasClientFeign contasClientFeign;
    private final ContaFacade contaFacade;
    
    @Autowired
    public ContaServiceImpl(ContasClientFeign contasClientFeign, ContaFacade contaFacade) {
        this.contasClientFeign = contasClientFeign;
        this.contaFacade = contaFacade;
    }
    
    @Override
    @Retry(name = "backendA", fallbackMethod = "fallbackRetry")
    @CircuitBreaker(name = "backendA", fallbackMethod = "fallbackCircuitBreaker")
    public UUID execute(Pair<ClienteResponseDTO, TransferenciaRequestDTO> objects) {
        return Optional.ofNullable(objects)
            .map(this::contaOrigem)
            .map(this::contaDestino)
            .map(contaFacade::executarTransferencia)
            .map(contasAtualizadas -> {
                final String idContaOrigem = contasAtualizadas.getValue0().getId();
                final String idContaDestino = contasAtualizadas.getValue1().getId();
                final ResponseEntity<?> contaOrigemSaldoAtualizado = contasClientFeign.atualizaSaldo(contasAtualizadas.getValue0());
                final ResponseEntity<?> contaDestinoSaldoAtualizado = contasClientFeign.atualizaSaldo(contasAtualizadas.getValue1());
                
                return UUID.randomUUID();
            })
            .orElseThrow(() -> new ContaProcessamentoException(ConstantesException.CONTA_FALHA_PROCESSAMENTO.getMensagem(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
    
    @Override
    public UUID fallbackRetry(Pair<ClienteResponseDTO, TransferenciaRequestDTO> objects, Exception e) {
        log.warn("Retentativas falharam, aplicando o método de fallback para o retry. Motivo: {}", e.getMessage());
        throw new ContaProcessamentoException( "Falha no processamento", HttpStatus.BAD_GATEWAY.value() );
    }
    
    @Override
    public UUID fallbackCircuitBreaker(Pair<ClienteResponseDTO, TransferenciaRequestDTO> objects, Exception e) {
        log.warn("Circuit Breaker ativado, aplicando o método de fallback para o Circuit Breaker. Motivo: {} ", e.getMessage());
        throw new ApiContasUnavailableException(ConstantesException.API_CONTA_INDISPONIVEL.getMensagem(), HttpStatus.SERVICE_UNAVAILABLE.value());
    }
    
    
    private Pair<ContaResponseDTO, TransferenciaRequestDTO> contaOrigem(Pair<ClienteResponseDTO, TransferenciaRequestDTO> objects) {
        return Optional.ofNullable(objects)
            .map(pair -> {
                log.info("Inciando consulta no Conta Origem: {} na Api de Conta ", pair.getValue1().getIdOrigem());
                return pair.getValue1().getIdOrigem();
            })
            .map(contasClientFeign::consultaContaPorIdConta)
            .map(contaOrigemResponse -> {
                log.info("Conta {}, está vincluada ao Cliente, transação em andamento", contaOrigemResponse.getId());
                return contaOrigemResponse;
            })
            .map(contaOrigem-> contaFacade.validarConta(contaOrigem, objects.getValue1().getValor()))
            .map(conta -> {
                log.info("Conta Origem: {} validada com sucesso, transação em andamento", conta.getId());
                return  Pair.with(conta, objects.getValue1());
            }).orElseThrow(() -> new ContaProcessamentoException(ConstantesException.CONTA_FALHA_PROCESSAMENTO.getMensagem(), HttpStatus.BAD_REQUEST.value()));
    }
    
    private Triplet<ContaResponseDTO, ContaResponseDTO, Double> contaDestino(Pair<ContaResponseDTO, TransferenciaRequestDTO> objects) {
        return Optional.ofNullable(objects)
            .map(dados -> {
                log.info("Inciando consulta Conta Destino: {} na Api de Conta ", dados.getValue1().getIdDestino());
                var contaDestino = contasClientFeign.consultaContaPorIdConta(dados.getValue1().getIdDestino());
                return Pair.with(dados.getValue0(), contaDestino);
            })
            .map(contas -> {
                log.info("Conta Destino: {} encontrada, transação em andamento", contas.getValue1().getId());
                return Triplet.with(contas.getValue0(), contas.getValue1(), objects.getValue1().getValor());
            }).orElseThrow(() -> new ContaProcessamentoException(ConstantesException.CONTA_FALHA_PROCESSAMENTO.getMensagem(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
