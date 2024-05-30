package com.desafioitau.api.transferencia.chain;

import com.desafioitau.api.transferencia.model.dto.ContaResponseDTO;
import com.desafioitau.api.transferencia.model.entity.Conta;
import org.springframework.stereotype.Component;

public interface ContaValidator {
    void setNext(ContaValidator next);
    void validar(Conta conta, double valorTransferencia);
}
