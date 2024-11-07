package com.br.bank.api_bank.infrastructure.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String error){
        super(error);
    }
}
