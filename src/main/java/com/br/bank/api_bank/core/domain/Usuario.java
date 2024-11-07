package com.br.bank.api_bank.core.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuario implements UserDetails {

    private Cpf cpf;
    private Password password;
    private LocalDateTime dataCriacao;
    private Role role;

    public Usuario(Cpf cpf, Password password, Role role) {
        this.cpf = cpf;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == Role.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (this.role == Role.USER) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_USER"));
        }
        else return List.of(new SimpleGrantedAuthority("ROLE_ROOT"));
    }

        @Override
        public String getPassword () {
            return password.getPassword();
        }

        @Override
        public String getUsername () {
            return cpf.getCpf();
        }
    }
