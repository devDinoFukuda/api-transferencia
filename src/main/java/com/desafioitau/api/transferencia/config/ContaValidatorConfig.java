package com.desafioitau.api.transferencia.config;

import com.desafioitau.api.transferencia.chain.ContaValidator;
import com.desafioitau.api.transferencia.chain.conta.PossuiSaldoValidator;
import com.desafioitau.api.transferencia.chain.conta.PossuiLimiteDiarioValidator;
import com.desafioitau.api.transferencia.chain.conta.ContaAtivaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContaValidatorConfig {
    
    @Autowired
    private PossuiSaldoValidator contaAtivaValidator;
    
    @Autowired
    private PossuiLimiteDiarioValidator possuiLimiteDiarioValidator;
    
    @Autowired
    private ContaAtivaValidator possuiSaldoValidator;
    
    @Bean
    public ContaValidator contaValidator() {
        contaAtivaValidator.setNext(possuiLimiteDiarioValidator);
        possuiLimiteDiarioValidator.setNext(possuiSaldoValidator);
        return contaAtivaValidator;
    }
}
