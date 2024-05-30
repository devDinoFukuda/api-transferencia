package com.desafioitau.api.transferencia.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import com.desafioitau.api.transferencia.model.entity.TransacaoEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
public class DynamoDBConfig {
    
    @Bean
    public  DynamoDBMapper dynamoDB () {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(
                new BasicAWSCredentials("test", "test")))
            .withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "us-east-1"))
            .build();
        try {
            DynamoDBMapper mapper = new DynamoDBMapper(client);
            ListTablesResult listTablesResult = client.listTables();
            
            if (listTablesResult.getTableNames().contains("Transacao")) {
               log.info("Tabela já criada.....");
            } else {
                CreateTableRequest req = mapper.generateCreateTableRequest(TransacaoEntity.class);
                req.withProvisionedThroughput(new ProvisionedThroughput(5L, 5L));
                client.createTable(req);
                log.info("Tabela criada com sucesso!");
            }
            return  mapper;
        } catch (Exception e) {
            log.error("Não foi possível criar a tabela. Erro: ".concat(e.getMessage()));
            throw new RuntimeException("Não foi possível criar a tabela. Erro: ".concat(e.getMessage()));
        }
    }
    
}
