package com.br.bank.api_bank.core.usecases.commands;

import com.br.bank.api_bank.core.domain.DateBirthday;
import com.br.bank.api_bank.core.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterCommand {

    private String cpf;
    private String name;
    private String password;

    private String email;
    private String phoneNumber;
    private DateBirthday dateBirthday;
    private String address;
    private Role role;

    public static RegisterCommand create(
            String cpf, String name, String password, String email,
            String phoneNumber, String dateBirthday,
            String address, String role) {

        return RegisterCommand.builder()
                .cpf(cpf)
                .name(name)
                .password(password)
                .email(email)
                .phoneNumber(phoneNumber)
                .dateBirthday(DateBirthday.convertDate(dateBirthday))
                .address(address)
                .role(Role.valueOf(role))
                .build();
    }
}
