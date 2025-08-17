package com.br.bank.api_bank.infrastructure.security;

import com.br.bank.api_bank.core.domain.Usuario;
import com.br.bank.api_bank.core.mappers.UsuarioEntityToDomain;
import com.br.bank.api_bank.core.ports.outbound.UsuarioRepository;
import com.br.bank.api_bank.infrastructure.exception.UserNotFoundException;
import com.br.bank.api_bank.infrastructure.persistence.entities.UsersEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = this.recoverToken(request);

            if (token != null) {
                String cpf = tokenService.extractCpfToken(token);

                Optional<UsersEntity> usuarioEntity = usuarioRepository.findByCpf(cpf);

                if (usuarioEntity.isEmpty()) {
                    log.warn("User not found with CPF: {}", cpf);
                    throw new UserNotFoundException("User not found with CPF: " + cpf);
                }

                Usuario usuario = UsuarioEntityToDomain.mapToDomain(usuarioEntity.get());
                var authetication = new UsernamePasswordAuthenticationToken(cpf, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authetication);

            }
        } catch (UserNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);

    }


    private String recoverToken(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
