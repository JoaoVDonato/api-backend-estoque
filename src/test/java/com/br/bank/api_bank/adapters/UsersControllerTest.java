package com.br.bank.api_bank.adapters;

import com.br.bank.api_bank.adapters.api.http.dto.LoginDTO;
import com.br.bank.api_bank.core.domain.Cpf;
import com.br.bank.api_bank.core.domain.Password;
import com.br.bank.api_bank.core.domain.Usuario;
import com.br.bank.api_bank.core.ports.outbound.UsersConfigRepository;
import com.br.bank.api_bank.core.ports.outbound.UsuarioRepository;
import com.br.bank.api_bank.infrastructure.persistence.entities.UsersEntity;
import com.br.bank.api_bank.infrastructure.security.TokenService;
import com.br.bank.api_bank.utils.UsuarioFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UsersControllerTest {

    private static String tokenAdmin1;
    private static String tokenUser1;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    UsersConfigRepository usersConfigRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    MockMvc mvc;

    @Autowired
    TokenService tokenService;

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres")
            .withDatabaseName("apibank")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.driverClassName", postgreSQLContainer::getDriverClassName);
    }

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }


    @BeforeEach
    public void setUp() {
        usersConfigRepository.deleteAll();
        usuarioRepository.deleteAll();

        UsersEntity userEntityAdmin = UsuarioFactory.createUserWithConfigAdmin();
        UsersEntity userEntity = UsuarioFactory.createUserWithConfig();
        usuarioRepository.saveAll(List.of(userEntityAdmin, userEntity));

        Usuario userDomainAdmin = UsuarioFactory.createUserDomainAdmin();
        Usuario userDomain = UsuarioFactory.createUserDomain();
        tokenAdmin1 = tokenService.gerarToken(userDomainAdmin);
        tokenUser1 = tokenService.gerarToken(userDomain);
    }

    @Test
    public void login_admin_sucessTest() throws Exception {

        LoginDTO login = UsuarioFactory.createLoginAdmin();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(login);
        Usuario userDomainAdmin = UsuarioFactory.createUserDomainAdmin();

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userDomainAdmin);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/bank/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login realizado com sucesso!"))
                .andExpect(jsonPath("$.token").value(tokenAdmin1));
    }

    @Test
    public void login_user_successTest() throws Exception {

        LoginDTO login = UsuarioFactory.createLoginUser();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(login);
        Usuario userDomain = UsuarioFactory.createUserDomain();

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userDomain);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/bank/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login realizado com sucesso!"))
                .andExpect(jsonPath("$.token").value(tokenUser1));
    }


    @Test
    public void login_user_notFoundTest() throws Exception{

        LoginDTO login = new LoginDTO("12345678901", "123");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(login);
        Usuario userDomainAdmin = UsuarioFactory.createUserDomainAdmin();
        userDomainAdmin.setCpf(Cpf.of("12345678901"));
        userDomainAdmin.setPassword(Password.of("123"));


        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userDomainAdmin);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/bank/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("message").value("User not found with CPF: 12345678901")); // Ajuste conforme a mensagem de erro esperada

    }

}
