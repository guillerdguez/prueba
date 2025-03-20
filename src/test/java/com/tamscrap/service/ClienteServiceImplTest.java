//package com.tamscrap.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import com.tamscrap.model.Cliente;
//import com.tamscrap.repository.ClienteRepo;
//import com.tamscrap.service.impl.ClienteServiceImpl;
//
//import jakarta.persistence.EntityExistsException;
//
//@ExtendWith(SpringExtension.class)
//public class ClienteServiceImplTest {
//
//	@Mock
//	private ClienteRepo clienteRepository;
//
//	@InjectMocks
//	private ClienteServiceImpl clienteService;
//
//	@Test
//	void cuandoInsertarClienteEntoncesClienteInsertadoOk() {
//		final Cliente cliente1 = new Cliente("NombreCliente1", "username1", "contrasena", "correo1", new HashSet<>());
//		when(clienteRepository.save(cliente1)).thenReturn(cliente1);
//		Cliente clienteExpected = clienteService.insertarCliente(cliente1);
//		verify(clienteRepository).save(cliente1);
//		assertNotNull(clienteExpected);
//		assertThat(cliente1).usingRecursiveAssertion().isEqualTo(clienteExpected);
//	}
//
//	@Test
//	void cuandoEliminarClienteEntoncesClienteEliminadoOk() {
//		doNothing().when(clienteRepository).deleteById(anyLong());
//		clienteService.eliminarCliente(anyLong());
//		verify(clienteRepository).deleteById(Mockito.any(Long.class));
//	}
//
//	@Test
//	void cuandoActualizarClienteEntoncesClienteActualizadoOk() {
//		final Cliente cliente1 = new Cliente("NombreCliente1", "username1", "contrasena", "correo1", new HashSet<>());
//		when(clienteRepository.findByUsername("username1")).thenReturn(Optional.of(cliente1));
//		when(clienteRepository.save(cliente1)).thenReturn(cliente1);
//		Cliente clienteObtenido = clienteService.obtenerPorUsername("username1");
//		clienteObtenido.setUsername("username1 Actualizado");
//		Cliente clienteGuardado = clienteService.insertarCliente(clienteObtenido);
//		verify(clienteRepository).save(clienteObtenido);
//		assertNotNull(clienteGuardado);
//		assertEquals("username1 Actualizado", clienteGuardado.getUsername());
//	}
//
//	@Test
//	void obtenerTodosOk() {
//		final Cliente cliente1 = new Cliente("NombreCliente1", "username1", "contrasena", "correo1", new HashSet<>());
//		final Cliente cliente2 = new Cliente("NombreCliente2", "username2", "contrasena", "correo2", new HashSet<>());
//		final Cliente cliente3 = new Cliente("NombreCliente3", "username3", "contrasena", "correo3", new HashSet<>());
//		when(clienteRepository.findAll()).thenReturn(List.of(cliente1, cliente2, cliente3));
//		List<Cliente> clientes = clienteService.obtenerTodos();
//		assertEquals(3, clientes.size());
//		assertTrue(clientes.containsAll(List.of(cliente1, cliente2, cliente3)));
//	}
//
//	@Test
//	void cuandoInsertarClienteConUsernameNuloEntoncesDevuelveExcepcion() {
//		final Cliente cliente1 = new Cliente(null, "username1", "contrasena", "correo1", new HashSet<>());
//		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//				() -> clienteService.insertarCliente(cliente1));
//		assertEquals("El nombre del cliente no puede ser nulo o vacío", exception.getMessage());
//	}
//
//	@Test
//	void cuandoInsertarClienteConUsernameYaInsertadoEntoncesDevuelveExcepcion() {
//		final Cliente cliente1 = new Cliente("NombreCliente1", "username1", "contrasena", "correo2", new HashSet<>());
//		when(clienteRepository.save(cliente1)).thenReturn(cliente1);
//		Cliente clienteExpected1 = clienteService.insertarCliente(cliente1);
//		verify(clienteRepository).save(cliente1);
//		assertNotNull(clienteExpected1);
//		assertThat(clienteExpected1).usingRecursiveAssertion().isEqualTo(cliente1);
//		final Cliente cliente2 = new Cliente("NombreCliente1", "username1", "contrasena", "correo1", new HashSet<>());
//		when(clienteRepository.save(cliente2)).thenThrow(new EntityExistsException());
//		assertThrows(EntityExistsException.class, () -> clienteService.insertarCliente(cliente2));
//	}
//
//	@Test
//	void cuandoInsertarClienteConEmailYaInsertadoEntoncesDevuelveExcepcion() {
//		final Cliente cliente1 = new Cliente("NombreCliente1", "username1", "contrasena", "correo1", new HashSet<>());
//		when(clienteRepository.save(cliente1)).thenReturn(cliente1);
//		Cliente clienteExpected1 = clienteService.insertarCliente(cliente1);
//		verify(clienteRepository).save(cliente1);
//		assertNotNull(clienteExpected1);
//		assertThat(clienteExpected1).usingRecursiveAssertion().isEqualTo(cliente1);
//		final Cliente cliente2 = new Cliente("NombreCliente2", "username2", "contrasena", "correo1", new HashSet<>());
//		when(clienteRepository.save(cliente2)).thenThrow(new EntityExistsException());
//		assertThrows(EntityExistsException.class, () -> clienteService.insertarCliente(cliente2));
//	}
//
//}
