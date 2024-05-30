package com.desafioitau.api.transferencia.service.impl;

import com.desafioitau.api.transferencia.errors.ConstantesException;
import com.desafioitau.api.transferencia.errors.exceptions.TransferenciaProcessamentoException;
import com.desafioitau.api.transferencia.model.dto.ClienteResponseDTO;
import com.desafioitau.api.transferencia.model.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.service.ApiService;
import lombok.extern.log4j.Log4j2;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class TransferenciaServiceImpl implements ApiService<Object, TransferenciaRequestDTO> {
    
    private final ApiService serviceCadastro;
    private final ApiService serviceConta;
    
    @Autowired
    public TransferenciaServiceImpl(CadastroServiceImpl serviceCadastro, ContaServiceImpl serviceConta) {
        this.serviceCadastro = serviceCadastro;
        this.serviceConta = serviceConta;
    }
    
    @Override
    public Object execute(TransferenciaRequestDTO transferenciaRequestDTO) {
        log.info("Iniciando processamento da transação solicitada");
        return Optional.ofNullable(transferenciaRequestDTO)
            .map(transferenciaRequestDTO1 -> serviceCadastro.execute(transferenciaRequestDTO1.getIdCliente()))
            .map(object -> {
                final ClienteResponseDTO clienteResponseDTO = (ClienteResponseDTO) object;
                return serviceConta.execute(Pair.with(clienteResponseDTO, transferenciaRequestDTO));
            })
            .orElseThrow(() -> new TransferenciaProcessamentoException(ConstantesException.TRANSFERENCIA_FALHA_PROCESSAMENTO.getMensagem()));
    }
    
    @Override
    public Object fallbackRetry(TransferenciaRequestDTO transferenciaRequestDTO, Exception e) {
        return null;
    }
    
    @Override
    public Object fallbackCircuitBreaker(TransferenciaRequestDTO transferenciaRequestDTO, Exception e) {
        return null;
    }
    
}
