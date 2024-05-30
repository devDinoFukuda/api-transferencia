package com.desafioitau.api.transferencia.model.entity;

import com.desafioitau.api.transferencia.chain.ContaValidator;
import com.desafioitau.api.transferencia.model.dto.ContaRequestDTO;
import com.desafioitau.api.transferencia.model.dto.ContaResponseDTO;
import com.desafioitau.api.transferencia.strategy.TransacaoStrategy;
import com.desafioitau.api.transferencia.strategy.transacoes.PagamentoStrategy;
import com.desafioitau.api.transferencia.strategy.transacoes.RecebimentoStrategy;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContaFacadeTest {
    
    @InjectMocks
    private ContaFacade contaFacade;
    
    @Mock
    private Map<TipoTransacao, TransacaoStrategy> estrategiasDeTransacao;
    @Mock
    private ContaValidator contaValidator;
    
   private  TransacaoStrategy pagamentoStrategy ;
    private TransacaoStrategy recebimentoStrategy;
    
    private ContaResponseDTO contaResponseDTO;
    
    @BeforeEach
    void setUp() {
        contaResponseDTO = ContaResponseDTO.builder()
            .id(UUID.randomUUID().toString())
            .saldo(500.00)
            .limiteDiario(500.00)
            .ativo(true)
            .build();
        
       pagamentoStrategy = new PagamentoStrategy();
       recebimentoStrategy = new RecebimentoStrategy();
    }
    
    @Test
    void testValidarConta() {
        ContaResponseDTO result = contaFacade.validarConta(contaResponseDTO, 200.00);
        assertEquals(contaResponseDTO.getId(), result.getId());
        assertEquals(contaResponseDTO.getSaldo(), result.getSaldo());
        assertEquals(contaResponseDTO.getLimiteDiario(), result.getLimiteDiario());
        assertTrue(result.isAtivo());
    }
    
    @Test
    void testExecutarTransferencia() {
        
        when(this.estrategiasDeTransacao.get(TipoTransacao.PAGAMENTO)).thenReturn(pagamentoStrategy);
        when(this.estrategiasDeTransacao.get(TipoTransacao.RECEBIMENTO)).thenReturn(recebimentoStrategy);
        ContaResponseDTO contaOrigem = contaResponseDTO;
        ContaResponseDTO contaDestino = contaResponseDTO;
        Double valorTransferencia = 200.00;
        Triplet<ContaResponseDTO, ContaResponseDTO, Double> dadosParaTransferencia =
            Triplet.with(contaOrigem, contaDestino, valorTransferencia);
        
        Pair<ContaRequestDTO, ContaRequestDTO> result = contaFacade.executarTransferencia(dadosParaTransferencia);
        
        assertEquals(contaOrigem.getId(), result.getValue0().getId());
        assertEquals(contaDestino.getId(), result.getValue1().getId());
    }
}