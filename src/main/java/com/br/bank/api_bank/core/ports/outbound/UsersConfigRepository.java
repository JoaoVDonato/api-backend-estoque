package com.br.bank.api_bank.core.ports.outbound;

import com.br.bank.api_bank.infrastructure.persistence.entities.UsersConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersConfigRepository extends JpaRepository<UsersConfigEntity, Long> {


}
