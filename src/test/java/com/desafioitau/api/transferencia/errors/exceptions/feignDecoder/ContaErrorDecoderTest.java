package com.desafioitau.api.transferencia.errors.exceptions.feignDecoder;

import com.desafioitau.api.transferencia.errors.exceptions.ClienteException;
import com.desafioitau.api.transferencia.errors.exceptions.ClienteProcessamentoException;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ContaErrorDecoderTest {
    
    private ContaErrorDecoder decoder = new ContaErrorDecoder();
    
    @Test
    void deveTestarContaErrorDecoder() {
        String key = "key";
        
        Response responseNotFound = Response.builder()
            .request(Request.create(Request.HttpMethod.GET, "/404", Collections.emptyMap(), null, StandardCharsets.UTF_8))
            .status(404).build();
        Exception exception = decoder.decode(key, responseNotFound);
        assertTrue(exception instanceof ClienteException);
        assertEquals(404, ((ClienteException) exception).getStatusCode());
        
        Response responseServerError = Response.builder()
            .request(Request.create(Request.HttpMethod.GET, "/500", Collections.emptyMap(), null, StandardCharsets.UTF_8))
            .status(500).build();
        exception = decoder.decode(key, responseServerError);
        assertTrue(exception instanceof ClienteProcessamentoException);
        assertEquals(500, ((ClienteProcessamentoException) exception).getStatusCode());
        
        Response responseOk = Response.builder()
            .request(Request.create(Request.HttpMethod.GET, "/200", Collections.emptyMap(), null, StandardCharsets.UTF_8))
            .status(200).build();
        assertNotNull(decoder.decode(key, responseOk));
    }
}