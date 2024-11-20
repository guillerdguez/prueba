package com.tamscrap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamscrap.config.SecurityConfig;
import com.tamscrap.model.Cliente;
import com.tamscrap.repository.ClienteRepo;
import com.tamscrap.repository.ProductoRepo;
import com.tamscrap.service.impl.ClienteServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    private static final String BASE_URL = "http://localhost:8082";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteServiceImpl clienteService;

    @MockBean
    private ProductoRepo productoRepository;

    @MockBean
    private ClienteRepo clienteRepository;


    @Test
    public void testWhen_guardarCliente_resultIsForbiddenTest() throws Exception {
        final Cliente cliente = new Cliente("username", "password","email", List.of());
        Mockito.when(clienteService.insertarCliente(cliente)).thenReturn(cliente);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/clientes/api/clientes"));

        resultActions.andExpect(status().isForbidden());
        Mockito.verifyNoInteractions(clienteService);
    }


    @Test
    public void testWhen_guardarCliente_resultIsOkTest() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final Cliente cliente = new Cliente("username", "password","email", List.of());
        Mockito.when(clienteService.insertarCliente(cliente)).thenReturn(cliente);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/clientes/api/clientes")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cliente))
                .with(user("user1")
                        .password("1234")
                        .roles("USER", "ADMIN"))
                .with(csrf()));

        resultActions
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Cliente clienteResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Cliente.class);
                    assertThat(clienteResponse).usingRecursiveComparison().isEqualTo(cliente);
                });
        verify(clienteService).insertarCliente(any(Cliente.class));
    }

}

