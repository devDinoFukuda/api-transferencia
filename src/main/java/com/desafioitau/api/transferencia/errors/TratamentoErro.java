package com.desafioitau.api.transferencia.errors;

import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
public class TratamentoErro implements Serializable {
    @Serial
    private static final long serialVersionUID = -1525254281912516680L;
    
    private LocalDateTime timestamp;
    private String path;
    private Integer status;
    private String error;
    private String message;
}
