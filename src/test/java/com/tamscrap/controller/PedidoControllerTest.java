package com.tamscrap.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamscrap.config.SecurityConfig;
import com.tamscrap.model.Cliente;
import com.tamscrap.model.Pedido;
import com.tamscrap.model.Producto;
import com.tamscrap.model.ProductosPedidos;
import com.tamscrap.repository.ClienteRepo;
import com.tamscrap.repository.PedidoRepo;
import com.tamscrap.repository.ProductoRepo;
import com.tamscrap.service.impl.ClienteServiceImpl;
import com.tamscrap.service.impl.PedidoServiceImpl;
import com.tamscrap.service.impl.ProductoServiceImpl;

@Import(SecurityConfig.class)
@WebMvcTest(PedidoController.class)
public class PedidoControllerTest {

	private static final String BASE_URL = "http://localhost:8082";
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ClienteRepo clienteRepo;
	@MockBean
	private ProductoRepo productoRepo;
	@MockBean
	private PedidoRepo pedidoRepo;

	@MockBean
	private ClienteServiceImpl clienteService;
	@MockBean
	private ProductoServiceImpl productoService;
	@MockBean
	private PedidoServiceImpl pedidoService;

	@Test
	public void testWhen_guardarPedido_resultIsForbiddenTest() throws Exception {
		final Producto producto = new Producto(null, "nombre", "", 1.0, "imagen", false, false, false, null, 0, false,
				null);
		final Cliente cliente = new Cliente("NombreDelCliente", "username", "password", "correo1",
				new HashSet<>());
		final Pedido pedido = new Pedido(cliente);

		ProductosPedidos productosPedidos1 = new ProductosPedidos(producto, pedido, 1);
		pedido.getProductos().add(productosPedidos1);
		Mockito.when(productoService.insertarProducto(producto)).thenReturn(producto);
		Mockito.when(clienteService.insertarCliente(cliente)).thenReturn(cliente);

		Mockito.when(pedidoService.insertarPedido(any(Pedido.class))).thenReturn(pedido);

		Mockito.when(pedidoService.insertarPedido(any(Pedido.class))).thenReturn(pedido);

		ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/pedidos/api/pedidos"));

		resultActions.andExpect(status().isForbidden());
		Mockito.verifyNoInteractions(pedidoService);
	}

	@Test
	public void guardarPedido_conAutenticacion_debeRetornarOk() throws Exception {
		final ObjectMapper objectMapper = new ObjectMapper();

		final Producto producto = new Producto(null, "nombre", "", 1.0, "imagen", false, false, false, null, 0, false,
				null);
		final Cliente cliente = new Cliente("NombreDelCliente", "username", "password", "correo1",
				new HashSet<>());
		final Pedido pedido = new Pedido(cliente);

		ProductosPedidos productosPedidos1 = new ProductosPedidos(producto, pedido, 1);
		pedido.getProductos().add(productosPedidos1);
		Mockito.when(productoService.insertarProducto(producto)).thenReturn(producto);
		Mockito.when(clienteService.insertarCliente(cliente)).thenReturn(cliente);

		Mockito.when(pedidoService.insertarPedido(any(Pedido.class))).thenReturn(pedido);

		ResultActions resultActions = mockMvc.perform(post("/pedidos/api/pedidos").contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(pedido))
				.with(user("user1").password("1234").roles("USER", "ADMIN")).with(csrf()));

		resultActions.andExpect(status().isOk()).andExpect(result -> {
			Pedido pedidoResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Pedido.class);
			assertThat(pedidoResponse).usingRecursiveComparison().isEqualTo(pedido);
		});

		verify(pedidoService).insertarPedido(any(Pedido.class));
	}

}
