package com.desafioitau.api.transferencia.config;

import com.desafioitau.api.transferencia.model.entity.TipoTransacao;
import com.desafioitau.api.transferencia.strategy.TransacaoStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class TransacaoStrategyConfig {
    
    @Bean
    public Map<TipoTransacao, TransacaoStrategy> estrategiasDeTransacao(List<TransacaoStrategy> strategies) {
        return strategies.stream()
            .collect(Collectors.toMap(TransacaoStrategy::getTipoTransacao, Function.identity()));
    }
}
