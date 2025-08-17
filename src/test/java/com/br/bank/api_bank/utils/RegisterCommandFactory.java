package com.br.bank.api_bank.utils;

import com.br.bank.api_bank.core.usecases.commands.RegisterCommand;

import java.time.LocalDateTime;

public class RegisterCommandFactory {

    public static RegisterCommand create(){

        return RegisterCommand.create("12345678913",
                "John",
                "12345",
                "john@john.com",
                "179977284444",
               "31/03/1996",
                "boulevard street",
                "ADMIN"
        );

    }


}
