package com.br.bank.api_bank.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
public enum Role {

    ADMIN("admin"),
    USER("user");

    private String role;

    Role(String role) {
        this.role = role;
    }

    public static Role fromString(String roles){
        for(Role role: Role.values()){
            if(role.getRole().equals(roles)){
                return role;
            }
        }
        log.warn("No role with the name {} was found", roles);
        throw new IllegalArgumentException("No role with the name" + roles + "was found");
    }





}
