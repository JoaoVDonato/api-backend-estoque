package com.br.bank.api_bank.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_config")
public class UsersConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @Column(name = "date_birthday", nullable = false)
    private LocalDate dateBirthday;
    @Column(nullable = false)
    private String address;
    @OneToOne
    @JoinColumn(name = "cpf", referencedColumnName = "cpf")
    private UsersEntity userAccount;

}
