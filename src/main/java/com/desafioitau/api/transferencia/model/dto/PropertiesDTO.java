package com.desafioitau.api.transferencia.model.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "properties")
public class PropertiesDTO {
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private Rest rest;
    
    @Getter
    @Setter(AccessLevel.PACKAGE)
    public static class Rest {
        private API api;
        
        @Getter
        @Setter(AccessLevel.PACKAGE)
        public static class API {
            private Endpoint cadastro;
            private Endpoint contas;
            private Endpoint bacen;
            
            @Getter
            @Setter(AccessLevel.PACKAGE)
            public static class Endpoint {
                private String url;
            }
        }
    }
    
}
