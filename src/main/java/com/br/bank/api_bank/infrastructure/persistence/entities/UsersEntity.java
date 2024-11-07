package com.br.bank.api_bank.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UsersEntity {

    @Id
    private String cpf;
    @Column(nullable = false)
    private String password;
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    @Column(nullable = false)
    private String role;
    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
    private UsersConfigEntity config;
}
