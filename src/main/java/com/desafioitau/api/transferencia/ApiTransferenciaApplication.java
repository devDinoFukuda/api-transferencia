package com.desafioitau.api.transferencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApiTransferenciaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiTransferenciaApplication.class, args);
	}

}
