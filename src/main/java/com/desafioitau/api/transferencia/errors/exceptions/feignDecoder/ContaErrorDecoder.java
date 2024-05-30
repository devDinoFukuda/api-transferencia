package com.desafioitau.api.transferencia.errors.exceptions.feignDecoder;

import com.desafioitau.api.transferencia.errors.ConstantesException;
import com.desafioitau.api.transferencia.errors.exceptions.ClienteException;
import com.desafioitau.api.transferencia.errors.exceptions.ClienteProcessamentoException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class ContaErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();
    
    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus httpStatus = HttpStatus.resolve(response.status());
        if (httpStatus != null && httpStatus.value() == 404) {
            return new ClienteException(ConstantesException.CLIENTE_NAO_ENCONTRADO.getMensagem() , httpStatus.value());
        }
        if (httpStatus != null && httpStatus.is5xxServerError()) {
            return new ClienteProcessamentoException(ConstantesException.CLIENTE_FALHA_API.getMensagem(), httpStatus.value());
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
    
}
