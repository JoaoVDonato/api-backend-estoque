package com.br.bank.api_bank.core.domain;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioConfig {

    private Name name;
    //TODO - verificar se vai ou não ser unique
    private Email email;
    private PhoneNumber phoneNumber;
    private LocalDate dateBirthday;
    private Address address;
}
