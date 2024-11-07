package com.br.bank.api_bank.adapters.api.http.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    private String login;
    private String password;
}
