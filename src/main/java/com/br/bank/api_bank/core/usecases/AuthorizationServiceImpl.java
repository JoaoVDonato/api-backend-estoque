    package com.br.bank.api_bank.core.usecases;

    import com.br.bank.api_bank.core.domain.Usuario;
    import com.br.bank.api_bank.core.mappers.UsuarioEntityToDomain;
    import com.br.bank.api_bank.core.ports.outbound.UsuarioRepository;
    import com.br.bank.api_bank.infrastructure.persistence.entities.UsersEntity;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;

    import java.util.Optional;

    @Slf4j
    @Service
    @RequiredArgsConstructor
    public class AuthorizationServiceImpl implements UserDetailsService {

        private final UsuarioRepository usuarioRepository;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

            Optional<UsersEntity> userEntity = usuarioRepository.findByCpf(username);
            if(userEntity.isEmpty()){
                log.error("Usuário não encontrado com o CPF: {} ", username);
                throw new UsernameNotFoundException("Usuário não encontrado com o CPF: " + username);
            }
            UsersEntity usersEntity = userEntity.get();
            return mapToDomain(usersEntity);
        }


        private Usuario mapToDomain(UsersEntity usersEntity) {

            return UsuarioEntityToDomain.mapToDomain(usersEntity);
        }
    }
