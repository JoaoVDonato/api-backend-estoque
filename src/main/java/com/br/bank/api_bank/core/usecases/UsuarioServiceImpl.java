package com.br.bank.api_bank.core.usecases;

import com.br.bank.api_bank.core.domain.Usuario;
import com.br.bank.api_bank.core.mappers.UsuarioEntityToDomain;
import com.br.bank.api_bank.core.ports.inbound.UsuarioService;
import com.br.bank.api_bank.core.ports.outbound.UsersConfigRepository;
import com.br.bank.api_bank.core.ports.outbound.UsuarioRepository;
import com.br.bank.api_bank.core.usecases.commands.RegisterCommand;
import com.br.bank.api_bank.infrastructure.exception.UserAlreadyExists;
import com.br.bank.api_bank.infrastructure.persistence.entities.UsersConfigEntity;
import com.br.bank.api_bank.infrastructure.persistence.entities.UsersEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsersConfigRepository usersConfigRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public Usuario registerUser(RegisterCommand registerCommand) {

        Optional<UsersEntity> cpf = usuarioRepository.findByCpf(registerCommand.getCpf());

        if (cpf.isPresent()) {
            log.error("Já existe um usuário para este cpf");
            throw new UserAlreadyExists("Já existe um usuário para este cpf");
        }

        UsersEntity usersEntity = createUser(registerCommand);
        UsersConfigEntity usersConfigEntity = createUserConfig(registerCommand, usersEntity);

        usuarioRepository.save(usersEntity);
        usersConfigRepository.save(usersConfigEntity);

        return mapper(usersEntity);
    }

    private UsersEntity createUser(RegisterCommand registerCommand) {

        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setCpf(registerCommand.getCpf());
        usersEntity.setPassword(encoder.encode(registerCommand.getPassword()));
        usersEntity.setRole(registerCommand.getRole().getRole());
        usersEntity.setDataCriacao(LocalDateTime.now());
        return usersEntity;
    }

    private UsersConfigEntity createUserConfig(RegisterCommand registerCommand, UsersEntity usersEntity) {
        UsersConfigEntity usersConfigEntity = new UsersConfigEntity();
        usersConfigEntity.setName(registerCommand.getName());
        usersConfigEntity.setEmail(registerCommand.getEmail());
        usersConfigEntity.setAddress(registerCommand.getAddress());
        usersConfigEntity.setPhoneNumber(registerCommand.getPhoneNumber());
        usersConfigEntity.setDateBirthday((registerCommand.getDateBirthday().getDate()));
        usersConfigEntity.setUserAccount(usersEntity);
        return usersConfigEntity;
    }

    private Usuario mapper(UsersEntity usuarioEntity) {
        return UsuarioEntityToDomain.mapToDomain(usuarioEntity);
    }
}
