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
public class Password {

    private String password;


    public static Password of(String value){

        if(value == null){
            log.error("A senha não pode ser nula");
            throw new IllegalArgumentException("A senha não pode ser nula");
        }else if(value.isBlank()){
            log.error("A senha não pode ficar em branco");
            throw new IllegalArgumentException("A senha não pode ficar em branco");
        }

        return new Password(value);
    }
}
