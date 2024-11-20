package com.tamscrap.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.tamscrap.model.Cliente;
import com.tamscrap.repository.ClienteRepo;
import com.tamscrap.service.impl.ClienteServiceImpl;

import jakarta.persistence.EntityExistsException;

@ExtendWith(SpringExtension.class)
public class ClienteServiceImplTest {

	@Mock
	private ClienteRepo clienteRepository;

	@InjectMocks
	private ClienteServiceImpl clienteService;

	@Test
	void cuandoInsertarClienteEntoncesClienteInsertadoOk() {
		Cliente clienteActual = new Cliente("username", "password", "email", List.of());
		when(clienteRepository.save(clienteActual)).thenReturn(clienteActual);

		Cliente clienteExpected = clienteService.insertarCliente(clienteActual);

		verify(clienteRepository).save(clienteActual);
		assertNotNull(clienteExpected);
		assertThat(clienteActual).usingRecursiveAssertion().isEqualTo(clienteExpected);
	}

	@Test
	void cuandoEliminarClienteEntoncesClienteEliminadoOk() {
		doNothing().when(clienteRepository).deleteById(anyLong());

		clienteService.eliminarCliente(anyLong());

		verify(clienteRepository).deleteById(Mockito.any(Long.class));
	}

	@Test
	void cuandoActualizarClienteEntoncesClienteActualizadoOk() {
 		Cliente clienteOriginal = new Cliente("Test", "password", "email", List.of());

 		when(clienteRepository.findByUsername("Test")).thenReturn(Optional.of(clienteOriginal));
 		when(clienteRepository.save(clienteOriginal)).thenReturn(clienteOriginal);

 		Cliente clienteObtenido = clienteService.obtenerPorUsername("Test");
		clienteObtenido.setUsername("Test Actualizado");
		Cliente clienteGuardado = clienteService.insertarCliente(clienteObtenido);

 		verify(clienteRepository).save(clienteObtenido);
		assertNotNull(clienteGuardado);
		assertEquals("Test Actualizado", clienteGuardado.getUsername());
	}

	@Test
	void obtenerTodosOk() {
 		Cliente cliente1 = new Cliente("Test1", "password1", "email1", List.of());
		Cliente cliente2 = new Cliente("Test2", "password2", "email2", List.of());
		Cliente cliente3 = new Cliente("Test3", "password3", "email3", List.of());

 		when(clienteRepository.findAll()).thenReturn(List.of(cliente1, cliente2, cliente3));
 
 		List<Cliente> clientes = clienteService.obtenerTodos();

 		assertEquals(3, clientes.size());
		assertTrue(clientes.containsAll(List.of(cliente1, cliente2, cliente3)));
	}

	@Test
	void cuandoInsertarClienteConUsernameNuloEntoncesDevuelveExcepcion() {
		Cliente clienteActual = new Cliente(null, "password", "email", List.of());

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> clienteService.insertarCliente(clienteActual));
		assertEquals("El nombre del cliente no puede ser nulo o vacío", exception.getMessage());
	}

	@Test
	void cuandoInsertarClienteConUsernameYaInsertadoEntoncesDevuelveExcepcion() {
		Cliente clienteActual1 = new Cliente("username", "password", "email2", List.of());
		when(clienteRepository.save(clienteActual1)).thenReturn(clienteActual1);

		Cliente clienteExpected1 = clienteService.insertarCliente(clienteActual1);

		verify(clienteRepository).save(clienteActual1);
		assertNotNull(clienteExpected1);
		assertThat(clienteExpected1).usingRecursiveAssertion().isEqualTo(clienteActual1);

		Cliente clienteActual2 = new Cliente("username", "password", "email", List.of());
		when(clienteRepository.save(clienteActual2)).thenThrow(new EntityExistsException());

		assertThrows(EntityExistsException.class, () -> clienteService.insertarCliente(clienteActual2));
	}

	@Test
	void cuandoInsertarClienteConEmailYaInsertadoEntoncesDevuelveExcepcion() {
		Cliente clienteActual1 = new Cliente("username", "password", "email", List.of());
		when(clienteRepository.save(clienteActual1)).thenReturn(clienteActual1);

		Cliente clienteExpected1 = clienteService.insertarCliente(clienteActual1);

		verify(clienteRepository).save(clienteActual1);
		assertNotNull(clienteExpected1);
		assertThat(clienteExpected1).usingRecursiveAssertion().isEqualTo(clienteActual1);

		Cliente clienteActual2 = new Cliente("username2", "password", "email", List.of());
		when(clienteRepository.save(clienteActual2)).thenThrow(new EntityExistsException());

		assertThrows(EntityExistsException.class, () -> clienteService.insertarCliente(clienteActual2));
	}

}