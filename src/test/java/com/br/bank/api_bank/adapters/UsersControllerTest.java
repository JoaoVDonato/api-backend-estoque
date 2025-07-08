package com.br.bank.api_bank.adapters;

import com.br.bank.api_bank.adapters.api.http.dto.LoginDTO;
import com.br.bank.api_bank.adapters.api.http.dto.RegisterDTO;
import com.br.bank.api_bank.core.domain.Cpf;
import com.br.bank.api_bank.core.domain.Usuario;
import com.br.bank.api_bank.core.ports.inbound.UsuarioService;
import com.br.bank.api_bank.core.ports.outbound.UsersConfigRepository;
import com.br.bank.api_bank.core.ports.outbound.UsuarioRepository;
import com.br.bank.api_bank.core.usecases.commands.RegisterCommand;
import com.br.bank.api_bank.infrastructure.persistence.entities.UsersEntity;
import com.br.bank.api_bank.infrastructure.security.TokenService;
import com.br.bank.api_bank.utils.RegisterCommandFactory;
import com.br.bank.api_bank.utils.UsuarioFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    UsuarioService usuarioService;

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

    ObjectMapper mapper;

    UsersEntity userEntityAdmin;
    UsersEntity userEntity;

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

    @AfterEach
    void afterEach(){
        usersConfigRepository.deleteAll();
        usuarioRepository.deleteAll();
    }


    @BeforeEach
    public void setUp() {
        mapper = new ObjectMapper();

        userEntityAdmin = UsuarioFactory.createUserWithConfigAdmin();
        userEntity = UsuarioFactory.createUserWithConfig();


        Usuario userDomainAdmin = UsuarioFactory.createUserDomainAdmin();
        Usuario userDomain = UsuarioFactory.createUserDomain();
        tokenAdmin1 = tokenService.gerarToken(userDomainAdmin);
        tokenUser1 = tokenService.gerarToken(userDomain);
    }

    @Test
    public void login_admin_sucessTest() throws Exception {

        LoginDTO login = UsuarioFactory.createLoginAdmin();
        String json = mapper.writeValueAsString(login);
        Usuario userDomainAdmin = UsuarioFactory.createUserDomainAdmin();
        usuarioRepository.saveAll(List.of(userEntityAdmin, userEntity));

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
        String json = mapper.writeValueAsString(login);
        Usuario userDomain = UsuarioFactory.createUserDomain();
        usuarioRepository.saveAll(List.of(userEntityAdmin, userEntity));


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
    public void login_admin_notFoundTest() throws Exception {

        LoginDTO login = new LoginDTO("12345678917", "1234");
        String json = mapper.writeValueAsString(login);

        Usuario userDomainAdmin = UsuarioFactory.createUserDomainAdmin();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UsernameNotFoundException("User not found with CPF: " + userDomainAdmin.getCpf().getCpf()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/bank/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("User not found with CPF: 12345678913"));
    }

    @Test
    @DisplayName("Deve registrar um usuário Admin e retornar status 200 ok com os dados salvos")
    public void testRegisterUserAdmin_Success() throws Exception {

        RegisterDTO registerAdminDTO = UsuarioFactory.createRegisteAdminDTO();
        RegisterCommand registerCommand = RegisterCommandFactory.create();
        Usuario userDomainAdmin = UsuarioFactory.createUserDomainAdmin();
        UsersEntity userEntityAdmin = UsuarioFactory.createUserWithConfigAdmin();

        when(usuarioService.registerUser(registerCommand)).thenReturn(userDomainAdmin);
        when(usuarioRepository.findByCpf("12345678913")).thenReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/bank/register")
                .header("Authorization", "Bearer " + tokenAdmin1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerAdminDTO));

        mvc.perform(request).andExpect(status().isOk());

       /* RegisterUserDTO registerUserDTO = UsuarioFactory.createRegisterUser();
        Usuario userDomain = UsuarioFactory.createUserDomain("00000011112", "admin", 1L);
        Usuario usuarioAdmin = UsuarioFactory.createUserDomain("11111111112", "admin", 1L);

        when(usuarioService.buscarUsuarioToken(tokenAdmin2)).thenReturn(usuarioAdmin);

        when(usuarioService.registerUser(
                any(RegisterUsuarioCommand.class),
                any(RegisterUsuarioConfigCommand.class)))
                .thenReturn(userDomain);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/nriskapp/registrar")
                .header("Authorization", "Bearer " + tokenAdmin2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerUserDTO));

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value(userDomain.getCpf().getValue()))
                .andExpect(jsonPath("$.tipo").value(userDomain.getRole().getRole()))
                .andExpect(jsonPath("$.nome").value(userDomain.getNome()))
                .andExpect(jsonPath("$.config.acesso").value(userDomain.getConfig().isAcesso()))
                .andExpect(jsonPath("$.config.frequenciaForaJanela").value(userDomain.getConfig().getFrequenciaForaJanela()))
                .andExpect(jsonPath("$.config.janelaAtiva.janelaInicio").value("01:05:00"))
                .andExpect(jsonPath("$.config.janelaAtiva.janelaFim").value("02:05:00"))
                .andExpect(jsonPath("$.config.janelaAtiva.frequencia").value(userDomain.getConfig().getJanelaAtiva().getFrequencia()))
                .andExpect(jsonPath("$.config.janelaAtiva.diasValidos", hasSize(userDomain.getConfig().getJanelaAtiva().getDiasValidos().size())))
                .andExpect(jsonPath("$.config.janelaAtiva.diasValidos[0]").value("SEG"))
                .andExpect(jsonPath("$.config.janelaAtiva.diasValidos[1]").value("TER"));*/
    }


}
