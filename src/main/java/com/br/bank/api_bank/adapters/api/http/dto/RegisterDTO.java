package com.br.bank.api_bank.adapters.api.http.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class RegisterDTO {

    private String cpf;
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private String dateBirthday;
    private String address;
    private String role;
}
