package com.desafioitau.api.transferencia.errors;

import com.desafioitau.api.transferencia.errors.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiTransferenciaExceptionHandlerTest {
    
    @InjectMocks
    private ApiTransferenciaExceptionHandler handler;
    
    private MockHttpServletRequest request;
    private WebRequest webRequest;
    
    @BeforeEach
    void setUp() {
        handler = new ApiTransferenciaExceptionHandler();
        request = new MockHttpServletRequest();
        webRequest = new ServletWebRequest(request);
    }
    @Test
    void deveTestarMethodArgumentNotValidException() {
        BindException bindException = new BindException(new Object(), "Object");
        bindException.addError(new FieldError("Object", "Field", "DefaultMessage"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindException);
        
        ResponseEntity<ValidatorErro> response =  handler.handler(ex, request);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        ValidatorErro erro = response.getBody();
        
        assertEquals("Field", erro.getError());
        assertEquals("DefaultMessage", erro.getMessage());
    }
    
    @Test
    void deveTestarClienteException() {
        ClienteException ex = new ClienteException("Test", HttpStatus.NOT_ACCEPTABLE.value());
        ResponseEntity<ValidatorErro> response = handler.handler(ex, request);
        
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        
        ValidatorErro error = response.getBody();
        
        assertEquals("Cadastro Cliente", error.getError());
        assertEquals("Test", error.getMessage());
    }
    
    @Test
    void deveTestarClienteProcessamentoException() {
        ClienteProcessamentoException ex = new ClienteProcessamentoException("Test", HttpStatus.NOT_ACCEPTABLE.value());
        ResponseEntity<ValidatorErro> response = handler.handler(ex, request);
        
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        
        ValidatorErro errorBody = response.getBody();
        
        assertEquals("Cadastro Cliente", errorBody.getError());
        assertEquals("Test", errorBody.getMessage());
        assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), errorBody.getStatus());
        }
    
    @Test
    public void deveTestarApiCadastroUnavailableException() {
        String exceptionMessage = "Test";
        int exceptionStatusCode = HttpStatus.NOT_FOUND.value();
        
        ApiCadastroUnavailableException exception =
            new ApiCadastroUnavailableException(exceptionMessage, exceptionStatusCode);
        
        
        ResponseEntity<ValidatorErro> responseEntity = handler.handler(exception, request);
        
        ValidatorErro responseBody = responseEntity.getBody();
        
        assertEquals(exceptionMessage, responseBody.getMessage());
        assertEquals("Cadastro Cliente", responseBody.getError());
        assertEquals(exceptionStatusCode, responseBody.getStatus());
    }
    
    @Test
    void deveTestarContaInativa() {
       
        MockHttpServletRequest request = new MockHttpServletRequest();
        ContaInativaException exception = new ContaInativaException("Conta Inativa");
        
        ResponseEntity<ValidatorErro> response =handler.handler(exception, request);
        
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Conta", response.getBody().getError());
        assertEquals("Conta Inativa", response.getBody().getMessage());
    }
    
    @Test
    void deveTestarLimiteDiarioExcedido() {
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        LimiteDiarioException exception = new LimiteDiarioException("Limite Excedido");
        
        ResponseEntity<ValidatorErro> response =handler.handler(exception, request);
        
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Conta Origem", response.getBody().getError());
        assertEquals("Limite Excedido", response.getBody().getMessage());
    }
    
    @Test
    void deveTestarSaldoInsuficiente() {
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        SaldoInsuficienteException exception = new SaldoInsuficienteException("Saldo insuficiente");
        
        ResponseEntity<ValidatorErro> response =handler.handler(exception, request);
        
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Conta Origem", response.getBody().getError());
        assertEquals("Saldo insuficiente", response.getBody().getMessage());
    }
    
    //TODO: finalizar testes
       
}