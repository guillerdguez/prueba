//package com.tamscrap.controller;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
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
//import com.tamscrap.model.Pedido;
//import com.tamscrap.model.UserAuthority;
//import com.tamscrap.repository.ClienteRepo;
//import com.tamscrap.repository.PedidoRepo;
//import com.tamscrap.repository.ProductoRepo;
//import com.tamscrap.service.impl.ClienteServiceImpl;
//import com.tamscrap.service.impl.PedidoServiceImpl;
//import com.tamscrap.service.impl.ProductoServiceImpl;
//
//@Import({
//    SecurityConfig.class,
//    JwtTokenUtil.class,
//    JwtRequestFilter.class,
//    JwtAuthenticationEntryPoint.class
//})
//@WebMvcTest(PedidoController.class)
//@TestPropertySource(properties = {
//    "jwt.secret=clave-secreta-muy-larga-de-256-bits-1234567890ABCDEF"
//})
//public class PedidoControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private PedidoServiceImpl pedidoService;
//
//    @MockBean
//    private ClienteServiceImpl clienteService;
//
//    @MockBean
//    private ProductoServiceImpl productoService;
//
//    @MockBean
//    private ClienteRepo clienteRepo;
//
//    @MockBean
//    private PedidoRepo pedidoRepo;
//
//    @MockBean
//    private ProductoRepo productoRepo;
//
//    @MockBean
//    private UserDetailsService userDetailsService;  
//
//    @Test
//    void pedidoNoCorrecto() throws Exception {
//        Pedido pedido = new Pedido();
//        pedido.setDireccionEnvio("Calle Falsa 123");
//        pedido.setMetodoPago("Tarjeta");
//
//        mockMvc.perform(post("/api/pedidos/addPedido")
//               .contentType(APPLICATION_JSON)
//               .content(new ObjectMapper().writeValueAsString(pedido)))
//               .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void pedidoCorrecto() throws Exception {
//        Set<UserAuthority> autoridades = new HashSet<>();
//        autoridades.add(UserAuthority.USER);
//        Cliente cliente = new Cliente("Usuario", "user", "pass", "user@test.com", autoridades);
//        cliente.setId(1L);
//
//        Pedido pedido = new Pedido(cliente);
//        pedido.setDireccionEnvio("Calle Real 456");
//        pedido.setMetodoPago("PayPal");
//
//        Mockito.when(clienteService.obtenerPorId(1L)).thenReturn(cliente);
//        Mockito.when(pedidoService.insertarPedido(any(Pedido.class))).thenReturn(pedido);
//
//        mockMvc.perform(post("/api/pedidos/addPedido")
//               .contentType(APPLICATION_JSON)
//               .content(new ObjectMapper().writeValueAsString(pedido))
//               .with(user(cliente))  
//               .with(csrf()))
//               .andExpect(status().isCreated());
//
//        verify(pedidoService).insertarPedido(any(Pedido.class));
//    }
//}