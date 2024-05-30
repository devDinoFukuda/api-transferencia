package com.desafioitau.api.transferencia.config;

import com.desafioitau.api.transferencia.model.dto.PropertiesDTO;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PropertiesDTO.class)
public class PropertiesConfig {
}
