package com.br.bank.api_bank.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    private String email;

    public static Email of(String value){

        if(value == null){
            log.error("O email não pode ser nulo");
            throw new IllegalArgumentException("O email não pode ser nulo");
        }else if(value.isBlank()){
            log.error("O email não pode ficar em branco");
            throw new IllegalArgumentException("O email não pode ficar em branco");
        }

        return new Email(value);
    }
}
