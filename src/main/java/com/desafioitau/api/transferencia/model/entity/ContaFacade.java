package com.desafioitau.api.transferencia.model.entity;

import com.desafioitau.api.transferencia.chain.ContaValidator;
import com.desafioitau.api.transferencia.model.dto.ContaRequestDTO;
import com.desafioitau.api.transferencia.model.dto.ContaResponseDTO;
import com.desafioitau.api.transferencia.strategy.TransacaoStrategy;
import lombok.extern.log4j.Log4j2;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Log4j2
@Component
public class ContaFacade {
    
    private final Map<TipoTransacao, TransacaoStrategy> estrategiasDeTransacao;
    private final ContaValidator contaValidator;
    
    @Autowired
    public ContaFacade(Map<TipoTransacao, TransacaoStrategy> estrategiasDeTransacao, ContaValidator contaValidator) {
        this.estrategiasDeTransacao = estrategiasDeTransacao;
        this.contaValidator = contaValidator;
    }
    
    public ContaResponseDTO validarConta(ContaResponseDTO contaResponseDTO,  double valorTransferencia) {
        log.info("Inciando validações da conta: {}", contaResponseDTO.getId());
        final Conta conta = new Conta(contaResponseDTO.getId(), contaResponseDTO.getSaldo(), contaResponseDTO.getLimiteDiario(), contaResponseDTO.isAtivo(), valorTransferencia, contaValidator);
        return ContaResponseDTO.builder()
            .id(conta.getId())
            .saldo(conta.getSaldo())
            .limiteDiario(conta.getLimiteDiario())
            .ativo(conta.isAtivo())
            .build();
    }
    
    public Pair<ContaRequestDTO, ContaRequestDTO> executarTransferencia(Triplet<ContaResponseDTO, ContaResponseDTO, Double> dadosParaTransferencia) {
        final Conta contaOrigemSaldoAtualizado = new Conta(dadosParaTransferencia.getValue0().getId(), dadosParaTransferencia.getValue0().getSaldo(), dadosParaTransferencia.getValue0().getLimiteDiario(),
            dadosParaTransferencia.getValue0().isAtivo(), dadosParaTransferencia.getValue2(), estrategiasDeTransacao.get(TipoTransacao.PAGAMENTO));
        final Conta contaDestinoSaldoAtualizado = new Conta(dadosParaTransferencia.getValue1().getId(), dadosParaTransferencia.getValue1().getSaldo(), dadosParaTransferencia.getValue1().getLimiteDiario(),
            dadosParaTransferencia.getValue1().isAtivo(), dadosParaTransferencia.getValue2(), estrategiasDeTransacao.get(TipoTransacao.RECEBIMENTO));
        final var requestContaOrigem = ContaRequestDTO.builder()
            .id(contaOrigemSaldoAtualizado.getId())
            .saldo(contaOrigemSaldoAtualizado.getSaldo())
            .ativo(contaOrigemSaldoAtualizado.isAtivo())
            .limiteDiario(contaOrigemSaldoAtualizado.getLimiteDiario())
            .build();
        final var requestContaDestino = ContaRequestDTO.builder()
            .id(contaDestinoSaldoAtualizado.getId())
            .saldo(contaDestinoSaldoAtualizado.getSaldo())
            .ativo(contaDestinoSaldoAtualizado.isAtivo())
            .limiteDiario(contaDestinoSaldoAtualizado.getLimiteDiario())
            .build();
        
        return Pair.with(requestContaOrigem, requestContaDestino);
    }
}
