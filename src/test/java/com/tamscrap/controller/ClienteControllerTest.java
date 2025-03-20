//package com.tamscrap.controller;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.tamscrap.config.JwtAuthenticationEntryPoint;
//import com.tamscrap.config.JwtRequestFilter;
//import com.tamscrap.config.JwtTokenUtil;
//import com.tamscrap.config.SecurityConfig;
//import com.tamscrap.model.Cliente;
//import com.tamscrap.model.UserAuthority;
//import com.tamscrap.repository.ClienteRepo;
//import com.tamscrap.repository.ProductoRepo;
//import com.tamscrap.service.impl.ClienteServiceImpl;
//
//@Import({
//    SecurityConfig.class,
//    JwtTokenUtil.class,
//    JwtRequestFilter.class,
//    JwtAuthenticationEntryPoint.class
//})
//@WebMvcTest(ClienteController.class)
//@TestPropertySource(properties = {
//    "jwt.secret=clave-secreta-muy-larga-de-256-bits-1234567890ABCDEF"
//})
//public class ClienteControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ClienteServiceImpl clienteService;
//
//    @MockBean
//    private ProductoRepo productoRepository;
//
//    @MockBean
//    private ClienteRepo clienteRepository;
//
//    @MockBean
//    private UserDetailsService userService;
//
//    // Test para editar cliente sin autenticación
//    @Test
//    public void testEditarCliente_SinAutenticacion_Forbidden() throws Exception {
//        Cliente cliente = new Cliente("NombreActualizado", "user", "pass", "email@test.com", new HashSet<>());
//        cliente.setId(1L);
//
//        mockMvc.perform(put("/api/clientes/editar/1")
//               .contentType(APPLICATION_JSON)
//               .content(new ObjectMapper().writeValueAsString(cliente)))
//               .andExpect(status().isUnauthorized());
//    }
//
//    // Test para editar cliente como ADMIN 
//    @Test
//    public void testEditarCliente_AutenticadoComoAdmin_Ok() throws Exception {
//        Cliente clienteExistente = new Cliente("NombreOriginal", "user", "pass", "email@test.com", new HashSet<>());
//        clienteExistente.setId(1L);
//
//        Set<UserAuthority> authorities = new HashSet<>();
//        authorities.add(UserAuthority.ADMIN); 
//
//        Cliente adminCliente = new Cliente("AdminName", "admin", "adminPass", "admin@test.com", authorities);
//        adminCliente.setId(999L);
//
//        when(clienteService.obtenerPorId(1L)).thenReturn(clienteExistente);
//        when(clienteService.insertarCliente(any(Cliente.class))).thenReturn(clienteExistente);
//
//        mockMvc.perform(put("/api/clientes/editar/1")
//               .contentType(APPLICATION_JSON)
//               .content(new ObjectMapper().writeValueAsString(clienteExistente))
//               .with(user(adminCliente))
//               .with(csrf()))
//               .andExpect(status().isOk());
//
//        verify(clienteService).insertarCliente(any(Cliente.class));
//    }
//    // Test para agregar favorito
//    @Test
//    public void testAgregarFavorito_Autenticado_Created() throws Exception {
//        Cliente cliente = new Cliente();
//        cliente.setId(1L);
//        
//        when(clienteService.agregarAFavoritos(anyLong(), anyLong())).thenReturn(cliente);
//
//        mockMvc.perform(post("/api/clientes/1/favorito/100")
//               .with(user("user1").roles("USER"))
//               .with(csrf()))
//               .andExpect(status().isCreated());
//    }
//}