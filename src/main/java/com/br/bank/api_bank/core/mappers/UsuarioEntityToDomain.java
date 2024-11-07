package com.br.bank.api_bank.core.mappers;

import com.br.bank.api_bank.core.domain.*;
import com.br.bank.api_bank.infrastructure.persistence.entities.UsersEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UsuarioEntityToDomain {

    public static Usuario mapToDomain(UsersEntity usersEntity) {
        try {

            return new Usuario(
                    Cpf.of(usersEntity.getCpf()),
                    Password.of(usersEntity.getPassword()),
                    usersEntity.getDataCriacao(),
                    Role.fromString(usersEntity.getRole()));
        } catch (Exception e) {
            log.warn("Erro ao mapear UsuarioEntity para Usuario: ", e);
            throw new RuntimeException(e);
        }
    }
}
