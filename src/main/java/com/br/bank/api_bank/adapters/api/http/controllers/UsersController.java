package com.br.bank.api_bank.adapters.api.http.controllers;

import com.br.bank.api_bank.adapters.api.http.dto.LoginDTO;
import com.br.bank.api_bank.adapters.api.http.dto.RegisterDTO;
import com.br.bank.api_bank.adapters.api.http.response.ApiResponse;
import com.br.bank.api_bank.adapters.api.http.response.ErrorResponse;
import com.br.bank.api_bank.adapters.api.http.response.LoginResponse;
import com.br.bank.api_bank.adapters.api.http.response.RegisterResponse;
import com.br.bank.api_bank.core.domain.Usuario;
import com.br.bank.api_bank.core.ports.inbound.UsuarioService;
import com.br.bank.api_bank.core.usecases.commands.LoginCommand;
import com.br.bank.api_bank.core.usecases.commands.RegisterCommand;
import com.br.bank.api_bank.infrastructure.exception.UserAlreadyExists;
import com.br.bank.api_bank.infrastructure.exception.UserNotFoundException;
import com.br.bank.api_bank.infrastructure.security.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
public class UsersController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;

    @PostMapping("/auth")
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid LoginDTO loginDTO) {

        try {
            LoginCommand loginCommand = LoginCommand.create(loginDTO.getLogin(), loginDTO.getPassword());

            var loginPassword = new UsernamePasswordAuthenticationToken(
                    loginCommand.getLogin().getCpf(),
                    loginCommand.getPassword().getPassword());

            Authentication authenticate = this.authenticationManager.authenticate(loginPassword);
            String token = tokenService.gerarToken((Usuario) authenticate.getPrincipal());

            LoginResponse tokenResponse = LoginResponse.builder()
                    .message("Login realizado com sucesso!")
                    .token(token)
                    .build();

            return ResponseEntity.ok().body(tokenResponse);
        } catch (UserNotFoundException | UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getLocalizedMessage()));
        }

    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterDTO registerDTO) {

        try {

            RegisterCommand registerCommand = RegisterCommand.create(
                    registerDTO.getCpf(), registerDTO.getName(),
                    registerDTO.getPassword(), registerDTO.getEmail(),
                    registerDTO.getPhoneNumber(), registerDTO.getDateBirthday(),
                    registerDTO.getAddress(), registerDTO.getRole());

            Usuario usuario = usuarioService.registerUser(registerCommand);

            return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse("Successfully registered user"));

        } catch (UserAlreadyExists e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage()));
        }
    }

    //TODO - criar uma forma de deletar usuario
    //TODO - criar um logout
}
