package com.br.bank.api_bank.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.br.bank.api_bank.core.domain.Usuario;
import org.springframework.beans.factory.annotation.Value;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Usuario usuario){
        try {

            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("api-bank")
                    .withSubject(usuario.getCpf().getCpf())
                    .withClaim("role", usuario.getRole().getRole())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);

        }catch (JWTCreationException jwtCreationException){
            throw new RuntimeException("Erro while generating token", jwtCreationException);
        }
    }

    public String extractCpfToken(String token){

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("api-bank")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTCreationException jwtCreationException){
            return "";
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.of("-03:00"));
    }
}
