package com.desafioitau.api.transferencia.model.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Builder;

import java.util.UUID;

@Builder
@DynamoDBTable(tableName = "Transacao")
public class TransacaoEntity {
    
    private UUID idTransacao;
    private String idContaOrigem;
    private String idContaDestino;
    private double valorTransferencia;;
    private String bacenNotificado;
    
    @DynamoDBHashKey(attributeName = "idTransacao")
    public UUID getIdTransacao() {
        return idTransacao;
    }
    
    @DynamoDBAttribute(attributeName = "idOrigem")
    public String getIdContaOrigem() {
        return idContaOrigem;
    }
    
    @DynamoDBAttribute(attributeName = "idDestino")
    public String getIdContaDestino() {
        return idContaDestino;
    }
    
    @DynamoDBAttribute(attributeName = "valorTransferencia")
    public double getValorTransferencia() {
        return valorTransferencia;
    }
    
    @DynamoDBAttribute(attributeName = "bacenNotificado")
    public String getBacenNotificado() {
        return bacenNotificado;
    }
}
