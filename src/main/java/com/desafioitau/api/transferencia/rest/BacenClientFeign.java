package com.desafioitau.api.transferencia.rest;

import com.desafioitau.api.transferencia.model.dto.ContaResponseDTO;
import com.desafioitau.api.transferencia.model.dto.NotificacaoRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "bacen", url = "${properties.rest.api.bacen.url}")
public interface BacenClientFeign {
    
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> notificarBacen(@RequestBody NotificacaoRequestDTO notificacaoRequestDTO);
}
