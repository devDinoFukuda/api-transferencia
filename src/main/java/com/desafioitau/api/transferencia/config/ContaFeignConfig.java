package com.desafioitau.api.transferencia.config;

import com.desafioitau.api.transferencia.errors.exceptions.feignDecoder.ClienteErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContaFeignConfig {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new ClienteErrorDecoder();
    }
}
