package com.desafioitau.api.transferencia.service.impl;

import com.desafioitau.api.transferencia.errors.ConstantesException;
import com.desafioitau.api.transferencia.errors.exceptions.*;
import com.desafioitau.api.transferencia.model.dto.ClienteResponseDTO;
import com.desafioitau.api.transferencia.model.dto.PropertiesDTO;
import com.desafioitau.api.transferencia.rest.CadastroClientFeign;
import com.desafioitau.api.transferencia.service.ApiService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class CadastroServiceImpl implements ApiService<ClienteResponseDTO, String> {
    
    private  final CadastroClientFeign cadastroClientFeign;
    
    @Autowired
    public CadastroServiceImpl(PropertiesDTO propertiesDTO, CadastroClientFeign cadastroClientFeign) {
        this.cadastroClientFeign = cadastroClientFeign;
    }
    
    @Override
    @Retry(name = "backendA", fallbackMethod = "fallbackRetry")
    @CircuitBreaker(name = "backendA", fallbackMethod = "fallbackCircuitBreaker")
    public ClienteResponseDTO execute(String idCliente) {
        log.info("Inciando consulta na Api de Cadastro pelo idCliente: {}", idCliente);
        return Optional.ofNullable(idCliente)
            .map(cadastroClientFeign::consultarCliente)
            .map(clienteResponseDTO  -> {
                log.info("Cliente {}, encontrado na base cadastral, transação em andamento", clienteResponseDTO.getId());
                return clienteResponseDTO;
            })
            .orElseThrow(() -> {
                log.error(ConstantesException.CLIENTE_NAO_ENCONTRADO.getMensagem());
                return new ClienteException(ConstantesException.CLIENTE_NAO_ENCONTRADO.getMensagem(),  null);
            });
    }
    
    @Override
    public ClienteResponseDTO fallbackRetry(String s, Exception e) {
         log.warn("Retentativas falharam, aplicando o método de fallback para o retry. Motivo: {}", e.getMessage());
        throw new ClienteProcessamentoException( ConstantesException.CLIENTE_FALHA_API.getMensagem(), HttpStatus.BAD_GATEWAY.value() );
    }
    
    @Override
    public ClienteResponseDTO fallbackCircuitBreaker(String s, Exception e) {
        log.warn("Circuit Breaker ativado, aplicando o método de fallback para o Circuit Breaker. Motivo: {} ", e.getMessage());
        throw new ApiCadastroUnavailableException(ConstantesException.API_CADASTRO_INDISPONIVEL.getMensagem(), HttpStatus.SERVICE_UNAVAILABLE.value());
    }
    
}
