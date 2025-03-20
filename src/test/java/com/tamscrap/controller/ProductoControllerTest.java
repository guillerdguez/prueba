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
//import com.tamscrap.model.Producto;
//import com.tamscrap.model.UserAuthority;
//import com.tamscrap.repository.ClienteRepo;
//import com.tamscrap.repository.ProductoRepo;
//import com.tamscrap.service.impl.ClienteServiceImpl;
//import com.tamscrap.service.impl.ProductoServiceImpl;
//
//@Import({ SecurityConfig.class, JwtTokenUtil.class, JwtRequestFilter.class, JwtAuthenticationEntryPoint.class })
//@WebMvcTest(ProductoController.class)
//@TestPropertySource(properties = { "jwt.secret=clave-secreta-muy-larga-de-256-bits-1234567890ABCDEF" })
//public class ProductoControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ProductoServiceImpl productoService;
//
//    @MockBean
//    private ClienteRepo clienteRepo;
//
//    @MockBean
//    private ClienteServiceImpl clienteService;
//
//    @MockBean
//    private ProductoRepo productoRepository;
//
//    @MockBean
//    private UserDetailsService userDetailsService;
//
//    @Test
//    void CreadoNoCorrecto() throws Exception {
//        Producto producto = new Producto(null, "nombre", "", 1.0, "imagen", false, false, false, null, 0, false, null);
//
//        mockMvc.perform(post("/api/producto/addProducto")
//               .contentType(APPLICATION_JSON)
//               .content(new ObjectMapper().writeValueAsString(producto)))
//               .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void CreadoCorrecto() throws Exception {
//        Producto producto = new Producto(null, "nombre", "", 1.0, "imagen", false, false, false, null, 0, false, null);
//        Set<UserAuthority> autoridades = new HashSet<>();
//        autoridades.add(UserAuthority.ADMIN);
//        Cliente admin = new Cliente("Admin", "admin", "pass", "admin@test.com", autoridades);
//
//        Mockito.when(productoService.insertarProducto(any(Producto.class))).thenReturn(producto);
//
//        mockMvc.perform(post("/api/producto/addProducto")
//               .contentType(APPLICATION_JSON)
//               .content(new ObjectMapper().writeValueAsString(producto))
//               .with(user(admin))
//               .with(csrf()))
//               .andExpect(status().isCreated());
//
//        verify(productoService).insertarProducto(any(Producto.class));
//    }
//}