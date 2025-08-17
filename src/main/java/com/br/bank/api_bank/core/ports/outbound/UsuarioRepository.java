package com.br.bank.api_bank.core.ports.outbound;

import com.br.bank.api_bank.infrastructure.persistence.entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsersEntity, String> {

    Optional<UsersEntity> findByCpf(String cpf);

}
