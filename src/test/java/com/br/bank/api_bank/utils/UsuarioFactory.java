package com.br.bank.api_bank.utils;

import com.br.bank.api_bank.adapters.api.http.dto.LoginDTO;
import com.br.bank.api_bank.core.domain.Cpf;
import com.br.bank.api_bank.core.domain.Password;
import com.br.bank.api_bank.core.domain.Role;
import com.br.bank.api_bank.core.domain.Usuario;
import com.br.bank.api_bank.infrastructure.persistence.entities.UsersConfigEntity;
import com.br.bank.api_bank.infrastructure.persistence.entities.UsersEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UsuarioFactory {

    public static Usuario createUserDomainAdmin() {
        return Usuario.builder()
                .cpf(Cpf.of("12345678913"))
                .password(Password.of("12345"))
                .dataCriacao(LocalDateTime.of(2024, 1, 1, 12, 1, 1))
                .role(Role.ADMIN)
                .build();
    }

    public static UsersConfigEntity createUserConfigEntityAdmin() {
        return UsersConfigEntity.builder()
                .id(1L)
                .name("John")
                .email("john@john.com")
                .dateBirthday(LocalDate.of(1996, 3, 31))
                .phoneNumber("14997284444")
                .address("boulevard street")
                .build();
    }

    public static UsersEntity createUserEntityAdmin() {
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();

        return UsersEntity.builder()
                .cpf("12345678913")
                .password(bCrypt.encode("12345"))
                .dataCriacao(LocalDateTime.of(2024, 1, 1, 12, 1, 1))
                .role("admin")
                .build();
    }

    public static Usuario createUserDomain() {
        return Usuario.builder()
                .cpf(Cpf.of("12345678914"))
                .password(Password.of("12345"))
                .dataCriacao(LocalDateTime.of(2024, 1, 1, 12, 1, 1))
                .role(Role.USER)
                .build();
    }

    public static UsersConfigEntity createUserConfigEntity() {
        return UsersConfigEntity.builder()
                .id(1L)
                .name("José")
                .email("jose@jose.com")
                .dateBirthday(LocalDate.of(1996, 3, 31))
                .phoneNumber("14997284444")
                .address("boulevard street")
                .build();
    }

    public static UsersEntity createUserEntity() {
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();

        return UsersEntity.builder()
                .cpf("12345678914")
                .password(bCrypt.encode("12345"))
                .dataCriacao(LocalDateTime.of(2024, 1, 1, 12, 1, 1))
                .role("user")
                .build();
    }

    public static UsersEntity createUserWithConfigAdmin() {
        UsersEntity userEntity = createUserEntityAdmin();
        UsersConfigEntity userConfigEntity = createUserConfigEntityAdmin();

        // Estabelece a relação entre `UsersEntity` e `UsersConfigEntity`
        userConfigEntity.setUserAccount(userEntity);
        userEntity.setConfig(userConfigEntity);

        return userEntity;
    }

    public static UsersEntity createUserWithConfig() {
        UsersEntity userEntity = createUserEntity();
        UsersConfigEntity userConfigEntity = createUserConfigEntity();

        // Estabelece a relação entre `UsersEntity` e `UsersConfigEntity`
        userConfigEntity.setUserAccount(userEntity);
        userEntity.setConfig(userConfigEntity);

        return userEntity;
    }

    public static LoginDTO createLoginAdmin() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin("12345678913");
        loginDTO.setPassword("12345");
        return loginDTO;
    }

    public static LoginDTO createLoginUser() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setLogin("12345678914");
        loginDTO.setPassword("12345");
        return loginDTO;
    }
}
