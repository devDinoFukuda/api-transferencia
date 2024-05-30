package com.desafioitau.api.transferencia.rest;

import com.desafioitau.api.transferencia.config.ClienteFeignConfig;
import com.desafioitau.api.transferencia.model.dto.ClienteResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cadastro", url = "${properties.rest.api.cadastro.url}", configuration = ClienteFeignConfig.class)
public interface CadastroClientFeign {
    
    @GetMapping(value = "/{idCliente}", produces = MediaType.APPLICATION_JSON_VALUE)
    ClienteResponseDTO consultarCliente(@PathVariable("idCliente") String idCliente);
}
