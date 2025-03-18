package com.tamscrap.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tamscrap.TestConfig;
import com.tamscrap.model.Cliente;
import com.tamscrap.model.Pedido;
import com.tamscrap.model.Producto;
import com.tamscrap.model.ProductosPedidos;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PedidoRepoTest {

	@Autowired
	private ClienteRepo clienteRepositorio;
	@Autowired
	private ProductoRepo productoRepositorio;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private PedidoRepo pedidoRepositorio;

	@Test
	public void pruebaCuandoSeGuardaEntoncesElementoEsEncontrado() {
		final Producto producto = new Producto(null, "nombre", "", 1.0, "imagen", false, false, false, null, 0, false, null);

		final Cliente cliente = new Cliente("NombreCliente1", "username1", "contrasena", "correo1", new HashSet<>());
		clienteRepositorio.save(cliente);

		final Pedido pedido = new Pedido(cliente);
		ProductosPedidos productosPedidos1 = new ProductosPedidos(producto, pedido, 1);
		pedido.getProductos().add(productosPedidos1);
		pedidoRepositorio.save(pedido);

		final Pedido pedido2 = pedidoRepositorio.getReferenceById(pedido.getId());

		assertThat(pedido2).usingRecursiveComparison().isEqualTo(pedido);
	}

	@Test
	public void pruebaCuandoSeEliminaElementoGuardadoEntoncesNoEsEncontrado() {
		final Producto producto = new Producto(null, "nombre", "", 1.0, "imagen", false, false, false, null, 0, false,
				null);
		productoRepositorio.save(producto);

		final Cliente cliente = new Cliente("NombreCliente1", "username1", "contrasena", "correo1", new HashSet<>());
		clienteRepositorio.save(cliente);

		final Pedido pedido = new Pedido(cliente);
		ProductosPedidos productosPedidos1 = new ProductosPedidos(producto, pedido, 1);
		pedido.getProductos().add(productosPedidos1);
		pedidoRepositorio.save(pedido);

		pedidoRepositorio.delete(pedido);
		final Optional<Pedido> pedido2 = pedidoRepositorio.findById(pedido.getId());

		assertThat(pedido2).isEmpty();
	}
}
