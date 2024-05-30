package com.desafioitau.api.transferencia.controller;

import com.desafioitau.api.transferencia.model.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.service.ApiService;
import com.desafioitau.api.transferencia.service.impl.BacenServiceImpl;
import com.desafioitau.api.transferencia.service.impl.TransferenciaServiceImpl;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.hamcrest.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferenciaController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class TransferenciaControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private TransferenciaServiceImpl transferenciaService;
    
    @MockBean
    private BacenServiceImpl bacenService;
    
    private UUID fakeId;
    private TransferenciaRequestDTO requestDTO;
    
    @BeforeEach
    void setup() {
        fakeId = UUID.randomUUID();
        requestDTO = TransferenciaRequestDTO.builder()
            .idCliente(UUID.randomUUID().toString())
            .idOrigem(UUID.randomUUID().toString())
            .idDestino(UUID.randomUUID().toString())
            .valor(5)
            .build();
    }
    
    @Test
    void quandoPostTransferencia_entaoRetornaStatusCreated() throws Exception {
        // Confição dos mocks
        when(transferenciaService.execute(requestDTO)).thenReturn(fakeId);
        when(bacenService.execute(Pair.with(requestDTO, fakeId))).thenReturn(null);
        
        // JSON de solicitação
        String transferenciaJson = "{"
            + "\"idCliente\": \"2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f\","
            + "\"valor\": 5,"
            + "\"idOrigem\": \"41313d7b-bd75-4c75-9dea-1f4be434007f\","
            + "\"idDestino\": \"d0d32142-74b7-4aca-9c68-838aeacef96b\""
            + "}";
        
        // Testa o endpoint /transferencia com método POST
        mockMvc.perform(MockMvcRequestBuilders.post("/transferencia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transferenciaJson))
            .andExpect(status().isCreated());
    }
}
