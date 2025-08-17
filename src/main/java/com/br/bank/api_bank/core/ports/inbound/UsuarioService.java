package com.br.bank.api_bank.core.ports.inbound;

import com.br.bank.api_bank.core.domain.Usuario;
import com.br.bank.api_bank.core.usecases.commands.RegisterCommand;

public interface UsuarioService {

    Usuario registerUser(RegisterCommand registerCommand);
}
