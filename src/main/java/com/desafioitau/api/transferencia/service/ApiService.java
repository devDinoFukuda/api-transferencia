package com.desafioitau.api.transferencia.service;

import org.springframework.stereotype.Service;

@Service
public interface    ApiService<Response, Paramameter> {
    Response execute(Paramameter paramameter);
    Response fallbackRetry(Paramameter paramameter, Exception e);
    Response fallbackCircuitBreaker(Paramameter paramameter, Exception e);
}
