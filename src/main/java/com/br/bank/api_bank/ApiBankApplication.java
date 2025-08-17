package com.br.bank.api_bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan
@SpringBootApplication
public class ApiBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiBankApplication.class, args);
	}

}
