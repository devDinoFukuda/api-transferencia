package com.desafioitau.api.transferencia.controller;

import com.desafioitau.api.transferencia.errors.ValidatorErro;
import com.desafioitau.api.transferencia.model.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.model.dto.TransferenciaResponseDTO;
import com.desafioitau.api.transferencia.service.ApiService;
import com.desafioitau.api.transferencia.service.impl.BacenServiceImpl;
import com.desafioitau.api.transferencia.service.impl.TransferenciaServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

/**
 * A classe TransferenciaController é responsável por manipular as solicitações HTTP relacionadas à transferência.
 * Ela expõe um ponto de extremidade para iniciar uma transferência e notificar o serviço Bacen sobre a transferência.
 *
 * @author Dino Fukuda
 * @version 1.0
 */
@Log4j2
@RestController
public class TransferenciaController {
    
    private  final ApiService transferenciaService;
    private final ApiService bacenService;
    
    @Autowired
    public TransferenciaController(TransferenciaServiceImpl transferenciaService, BacenServiceImpl bacenService) {
        this.transferenciaService = transferenciaService;
        this.bacenService = bacenService;
    }
    
    @Operation(summary = "Processa uma nova transferência")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transferência executada com sucesso",
            content = { @Content(mediaType = "application/json",
                schema = @Schema(implementation = TransferenciaResponseDTO.class)) }),
        @ApiResponse(responseCode = "400", description = "Campo enviado no corpo da requisição não atende aos critérios",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = ValidatorErro.class))}),
        @ApiResponse(responseCode = "404",
            description = "Cliente não encontrado ou Conta não encontrada",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = ValidatorErro.class))}),
        @ApiResponse(responseCode = "406",
            description = "Conta Inativa e/ou Limite Diário excedido e/ou não existe || Saldo Insuficiente",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = ValidatorErro.class))}),
        @ApiResponse(responseCode = "500",
            description = "Falha de processamento Fluxo Cadastral, Conta ou de Transferência",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = ValidatorErro.class))}),
        @ApiResponse(responseCode = "502",
            description = "Falha na API de Cadastro ou na API de Contas",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = ValidatorErro.class))}),
        @ApiResponse(responseCode = "503",
            description = "API de Casdastro ou API de Contas indisponivel",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = ValidatorErro.class))})
    })
    @PostMapping("/transferencia")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TransferenciaResponseDTO> efetuarTransferencia(@Valid @RequestBody TransferenciaRequestDTO transferenciaRequestDTO)
    {
        final UUID idTransferencia = (UUID) transferenciaService.execute(transferenciaRequestDTO);
        
        bacenService.execute(Pair.with(transferenciaRequestDTO, idTransferencia));
        
        final TransferenciaResponseDTO transferenciaExecutada = TransferenciaResponseDTO.builder()
            .idTransferencia(idTransferencia)
            .build();
     
        log.info("Transação concluida com sucesso, idTransacao: {}", transferenciaExecutada.getIdTransferencia());
        return ResponseEntity.created(this.getUri(transferenciaExecutada.getIdTransferencia()))
            .body( transferenciaExecutada);
    }
    
    private URI getUri(UUID id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri();
    }
}
