package com.br.bank.api_bank.core.usecases.commands;

import com.br.bank.api_bank.adapters.api.http.dto.LoginDTO;
import com.br.bank.api_bank.core.domain.Cpf;
import com.br.bank.api_bank.core.domain.Password;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginCommand {

    private Cpf login;
    private Password password;

    public static LoginCommand create(String login, String password){
        return LoginCommand.builder()
                .login(Cpf.of(login))
                .password(Password.of(password))
                .build();
    }
}
