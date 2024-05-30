package com.desafioitau.api.transferencia.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class TransferenciaResponseDTO {
    private UUID idTransferencia;
}
