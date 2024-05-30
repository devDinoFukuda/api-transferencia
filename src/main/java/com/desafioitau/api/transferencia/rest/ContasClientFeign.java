package com.desafioitau.api.transferencia.rest;

import com.desafioitau.api.transferencia.model.dto.ClienteResponseDTO;
import com.desafioitau.api.transferencia.model.dto.ContaRequestDTO;
import com.desafioitau.api.transferencia.model.dto.ContaResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "contas", url = "${properties.rest.api.contas.url}")
public interface ContasClientFeign {
    
    @GetMapping(value = "/{idConta}", produces = MediaType.APPLICATION_JSON_VALUE)
    ContaResponseDTO consultaContaPorIdConta(@PathVariable("idConta") String idConta);
    
    @PutMapping(value = "/saldos", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> atualizaSaldo(ContaRequestDTO contaRequestDTO);
}
