package com.tamscrap.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

import jakarta.persistence.EntityExistsException;

@ExtendWith(SpringExtension.class)
public class PedidoServiceImplTest {

	@Mock
	private ClienteRepo clienteRepository;
	@Mock
	private ProductoRepo productoRepository;
	@Mock
	private PedidoRepo pedidoRepository;

	@InjectMocks
	private ClienteServiceImpl clienteService;
	@InjectMocks
	private ProductoServiceImpl productoService;
	@InjectMocks
	private PedidoServiceImpl pedidoService;

	@Test
	void cuandoInsertarPedidoEntoncesPedidoInsertadoOk() {
		Cliente cliente = new Cliente("NombreCliente1", "username", "password", "email", new HashSet<>());
		when(clienteRepository.save(cliente)).thenReturn(cliente);
		final Producto producto = new Producto(null, "nombre", "", 1.0, "imagen", false, false, false, null, 0, false,
				null); 
		when(productoRepository.save(producto)).thenReturn(producto);
		Pedido pedido = new Pedido(cliente);
		ProductosPedidos productosPedidos1 = new ProductosPedidos(producto, pedido, 1);
		pedido.getProductos().add(productosPedidos1);
		when(pedidoRepository.save(pedido)).thenReturn(pedido);
		Pedido PedidoEsperado = pedidoService.insertarPedido(pedido);

		verify(pedidoRepository).save(pedido);
		assertNotNull(PedidoEsperado);
		assertThat(pedido).usingRecursiveAssertion().isEqualTo(PedidoEsperado);
	}

	@Test
	void cuandoEliminarPedidoEntoncesPedidoEliminadoOk() {
		doNothing().when(pedidoRepository).deleteById(anyLong());

		pedidoService.eliminarPedido(anyLong());

		verify(pedidoRepository).deleteById(Mockito.any(Long.class));
	}

	@Test
	void cuandoActualizarPedidoEntoncesPedidoActualizadoOk() {
		Cliente cliente = new Cliente("NombreCliente1", "username", "password", "email", new HashSet<>());
		final Producto producto  = new Producto(null, "nombre", "", 1.0, "imagen", false, false, false, null, 0, false,
				null);

		Pedido pedido = new Pedido(cliente);
		ProductosPedidos productosPedidos1 = new ProductosPedidos(producto, pedido, 1);
		pedido.getProductos().add(productosPedidos1);

		when(pedidoRepository.findById(anyLong())).thenReturn(Optional.of(pedido));
		when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

		pedido.addProducto2(producto, 5);
		Pedido pedidoGuardado = pedidoService.insertarPedido(pedido);

		List<ProductosPedidos> productosList = new ArrayList<>(pedido.getProductos());

		verify(pedidoRepository).save(pedido);
		assertNotNull(pedidoGuardado);
		assertEquals(5, productosList.get(0).getCantidad());
	}

	@Test
	void obtenerTodosOk() {
		Cliente cliente = new Cliente("NombreCliente1", "username", "password", "email", new HashSet<>());
		final Producto producto1 = new Producto(null, "nombre", "", 1.0, "imagen", false, false, false, null, 0, false,
				null);
		final Producto producto2 = new Producto(null, "nombre2", "", 1.0, "imagen", false, false, false, null, 0, false,
				null);
		final Producto producto3 = new Producto(null, "nombre3", "", 1.0, "imagen", false, false, false, null, 0, false,
				null);

		Pedido pedido1 = new Pedido(cliente);
		ProductosPedidos productosPedidos1 = new ProductosPedidos(producto1, pedido1, 1);
		pedido1.getProductos().add(productosPedidos1);

		Pedido pedido2 = new Pedido(cliente);
		ProductosPedidos productosPedidos2 = new ProductosPedidos(producto2, pedido2, 1);
		pedido2.getProductos().add(productosPedidos2);

		Pedido pedido3 = new Pedido(cliente);
		ProductosPedidos productosPedidos3 = new ProductosPedidos(producto3, pedido3, 1);
		pedido3.getProductos().add(productosPedidos3);

		when(pedidoRepository.findAll()).thenReturn(List.of(pedido1, pedido2, pedido3));

		List<Pedido> pedidos = pedidoService.obtenerTodos();

		assertEquals(3, pedidos.size());
		assertTrue(pedidos.containsAll(List.of(pedido1, pedido2, pedido3)));
	}

	@Test
	void cuandoInsertarPedidoConUsernameNuloEntoncesDevuelveExcepcion() {
		Cliente clienteActual = new Cliente(null, "username", "password", "email", new HashSet<>());

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> clienteService.insertarCliente(clienteActual));
		assertEquals("El nombre del cliente no puede ser nulo o vacío", exception.getMessage());
	}

	@Test
	void cuandoInsertarPedidoConUsernameYaInsertadoEntoncesDevuelveExcepcion() {
		Cliente clienteActual1 = new Cliente("NombreCliente1", "username", "password", "email2", new HashSet<>());
		when(clienteRepository.save(clienteActual1)).thenReturn(clienteActual1);

		Cliente clienteExpected1 = clienteService.insertarCliente(clienteActual1);

		verify(clienteRepository).save(clienteActual1);
		assertNotNull(clienteExpected1);
		assertThat(clienteExpected1).usingRecursiveAssertion().isEqualTo(clienteActual1);

		Cliente clienteActual2 = new Cliente("NombreCliente1", "username", "password", "email", new HashSet<>());
		when(clienteRepository.save(clienteActual2)).thenThrow(new EntityExistsException());

		assertThrows(EntityExistsException.class, () -> clienteService.insertarCliente(clienteActual2));
	}

	@Test
	void cuandoInsertarPedidoConEmailYaInsertadoEntoncesDevuelveExcepcion() {
		Cliente clienteActual1 = new Cliente("NombreCliente1", "username", "password", "email", new HashSet<>());
		when(clienteRepository.save(clienteActual1)).thenReturn(clienteActual1);

		Cliente clienteExpected1 = clienteService.insertarCliente(clienteActual1);

		verify(clienteRepository).save(clienteActual1);
		assertNotNull(clienteExpected1);
		assertThat(clienteExpected1).usingRecursiveAssertion().isEqualTo(clienteActual1);

		Cliente clienteActual2 = new Cliente("NombreCliente2", "username2", "password", "email", new HashSet<>());
		when(clienteRepository.save(clienteActual2)).thenThrow(new EntityExistsException());

		assertThrows(EntityExistsException.class, () -> clienteService.insertarCliente(clienteActual2));
	}

}
