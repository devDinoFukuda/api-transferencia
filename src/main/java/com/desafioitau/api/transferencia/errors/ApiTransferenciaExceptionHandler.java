package com.desafioitau.api.transferencia.errors;

import com.desafioitau.api.transferencia.errors.exceptions.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Esta classe é responsável por manipular exceções relacionadas às operações de transferência da API.
 *
 * @author Dino Fukuda
 * @version 1.0
 */
@Log4j2
@RestControllerAdvice
public class ApiTransferenciaExceptionHandler {
    private final AtomicReference<ValidatorErro> error  = new AtomicReference<>();
    private final AtomicReference<String> fieldName = new AtomicReference<>();
    private final AtomicReference<String> errorMessage = new AtomicReference<>();
    private final AtomicInteger  statusCode = new AtomicInteger();
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidatorErro> handler(MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        ex.getBindingResult().getAllErrors().forEach(erro -> {
            fieldName.set(((FieldError) erro).getField());
            errorMessage.set( erro.getDefaultMessage());
            error.set(new ValidatorErro(this.timestamp(), request.getContextPath(), HttpStatus.BAD_REQUEST.value(),
                fieldName.get(), errorMessage.get())); ;
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(error.get());
    }
    
    
    @ExceptionHandler(ClienteException.class)
    public ResponseEntity<ValidatorErro> handler(ClienteException ex, HttpServletRequest request) {
        fieldName.set("Cadastro Cliente");
        errorMessage.set(ex.getMessage());
        statusCode.set( ex.getStatusCode());
        
        error.set(new ValidatorErro(this.timestamp(), request.getContextPath(), statusCode.get(),
            fieldName.get(), errorMessage.get())); ;
            
        log.error(fieldName.get().concat(": ".concat(errorMessage.get())));
        return ResponseEntity.status(HttpStatus.valueOf(statusCode.get()))
            .body(error.get());
    }
    
   @ExceptionHandler(ClienteProcessamentoException.class)
    public ResponseEntity<ValidatorErro> handler(ClienteProcessamentoException ex, HttpServletRequest request) {
       fieldName.set("Cadastro Cliente");
       errorMessage.set(ex.getMessage());
       statusCode.set( ex.getStatusCode());
       
       error.set(new ValidatorErro(this.timestamp(), request.getContextPath(), statusCode.get(),
           fieldName.get(), errorMessage.get())); ;
       
       log.error(fieldName.get().concat(": ".concat(errorMessage.get())));
       return ResponseEntity.status(HttpStatus.valueOf(statusCode.get()))
           .body(error.get());
    }
    
    @ExceptionHandler(ApiCadastroUnavailableException.class)
    public ResponseEntity<ValidatorErro> handler(ApiCadastroUnavailableException ex, HttpServletRequest request) {
        fieldName.set("Cadastro Cliente");
        errorMessage.set(ex.getMessage());
        statusCode.set( ex.getStatusCode());
        
        error.set(new ValidatorErro(this.timestamp(), request.getContextPath(), statusCode.get(),
            fieldName.get(), errorMessage.get())); ;
        
        log.error(fieldName.get().concat(": ".concat(errorMessage.get())));
        return ResponseEntity.status(HttpStatus.valueOf(statusCode.get()))
            .body(error.get());
    }
    
    @ExceptionHandler(ContaInativaException.class)
    public ResponseEntity<ValidatorErro> handler(ContaInativaException ex, HttpServletRequest request) {
        fieldName.set("Conta");
        errorMessage.set(ex.getMessage());
        
        error.set(new ValidatorErro(this.timestamp(), request.getContextPath(), HttpStatus.NOT_ACCEPTABLE.value(),
            fieldName.get(), errorMessage.get())); ;
        
        log.error(fieldName.get().concat(": ".concat(errorMessage.get())));
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
            .body(error.get());
    }
    
    
    @ExceptionHandler(LimiteDiarioException.class)
    public ResponseEntity<ValidatorErro> handler(LimiteDiarioException ex, HttpServletRequest request) {
        fieldName.set("Conta Origem");
        errorMessage.set(ex.getMessage());
        
        error.set(new ValidatorErro(this.timestamp(), request.getContextPath(), HttpStatus.NOT_ACCEPTABLE.value(),
            fieldName.get(), errorMessage.get())); ;
        
        log.error(fieldName.get().concat(": ".concat(errorMessage.get())));
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
            .body(error.get());
    }
    
    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<ValidatorErro> handler(SaldoInsuficienteException ex, HttpServletRequest request) {
        fieldName.set("Conta Origem");;
        errorMessage.set(ex.getMessage());
        error.set(new ValidatorErro(this.timestamp(), request.getContextPath(), HttpStatus.NOT_ACCEPTABLE.value(),
            fieldName.get(), errorMessage.get())); ;
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
            .body(error.get());
    }
    
    
    @ExceptionHandler(ContaProcessamentoException.class)
    public ResponseEntity<ValidatorErro> handler(ContaProcessamentoException ex, HttpServletRequest request) {
        fieldName.set("Conta ");
        errorMessage.set(ex.getMessage());
        statusCode.set( ex.getStatusCode());
        
        error.set(new ValidatorErro(this.timestamp(), request.getContextPath(), statusCode.get(),
            fieldName.get(), errorMessage.get())); ;
        
        log.error(fieldName.get().concat(": ".concat(errorMessage.get())));
        return ResponseEntity.status(HttpStatus.valueOf(statusCode.get()))
            .body(error.get());
    }
    
    @ExceptionHandler(ApiContasUnavailableException.class)
    public ResponseEntity<ValidatorErro> handler(ApiContasUnavailableException ex, HttpServletRequest request) {
        fieldName.set("Conta");
        errorMessage.set(ex.getMessage());
        statusCode.set( ex.getStatusCode());
        
        error.set(new ValidatorErro(this.timestamp(), request.getContextPath(), statusCode.get(),
            fieldName.get(), errorMessage.get())); ;
        
        log.error(fieldName.get().concat(": ".concat(errorMessage.get())));
        return ResponseEntity.status(HttpStatus.valueOf(statusCode.get()))
            .body(error.get());
    }
    
    private LocalDateTime timestamp() {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return zonedDateTime.toLocalDateTime();
    }
    
}
