package com.tamscrap.controller;

import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tamscrap.dto.CarritoDTO;
import com.tamscrap.dto.CarritoProductoDTO;
import com.tamscrap.dto.ProductoDTO;
import com.tamscrap.model.Carrito;
import com.tamscrap.model.Cliente;
import com.tamscrap.model.Producto;
import com.tamscrap.service.impl.ClienteServiceImpl;
import com.tamscrap.service.impl.ProductoServiceImpl;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = { "http://localhost:4200", "https://tamscrapt.up.railway.app" })
public class CarritoController {

	private static final Logger logger = Logger.getLogger(CarritoController.class.getName());

	@Autowired
	private ProductoServiceImpl productoService;

	@Autowired
	private ClienteServiceImpl clienteService;

	private Carrito obtenerOCrearCarrito(Cliente cliente) {
		if (cliente.getCarrito() == null) {
			Carrito nuevoCarrito = new Carrito(cliente.getNombre());
			cliente.setCarrito(nuevoCarrito);
			clienteService.insertarCliente(cliente);
		}
		return cliente.getCarrito();
	}

	@PostMapping("/addProducto/{id}/{cantidad}")
	public ResponseEntity<Void> agregarProductoAlCarrito(@PathVariable Long id, @PathVariable int cantidad) {
		logger.info("Agregando producto al carrito con ID: " + id + " y cantidad: " + cantidad);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal().equals("anonymousUser")) {
			logger.warning("Cliente no autenticado");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		String username = authentication.getName();
		Cliente cliente = clienteService.obtenerPorUsername(username);
		if (cliente == null) {
			logger.warning("Cliente no encontrado para el usuario: " + username);
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		Producto producto = productoService.obtenerPorId(id).orElseThrow();
		if (producto == null) {
			logger.warning("Producto no encontrado con ID: " + id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Carrito carrito = obtenerOCrearCarrito(cliente);
		carrito.addProducto(producto, cantidad);
		clienteService.insertarCliente(cliente);
		logger.info("Producto agregado al carrito exitosamente");
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/productos/{userId}")
	public ResponseEntity<CarritoDTO> mostrarProductosCarrito(@PathVariable Long userId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal().equals("anonymousUser")) {
			logger.warning("Cliente no autenticado");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String username = authentication.getName();
		Cliente cliente = clienteService.obtenerPorUsername(username);

		if (cliente == null || !cliente.getId().equals(userId)) {
			logger.warning("El cliente autenticado no tiene acceso al carrito solicitado o no existe.");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		Carrito carrito = cliente.getCarrito();
		if (carrito == null || carrito.getProductos().isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		Set<CarritoProductoDTO> productosDTO = carrito.getProductos().stream().map(cp -> {
			Producto p = cp.getProducto();
			ProductoDTO productoDTO = new ProductoDTO(p.getId(), p.getNombre(), p.getPrecio(), p.getImagen(),
					p.getCantidad());
			return new CarritoProductoDTO(cp.getId(), productoDTO, cp.getCantidad());
		}).collect(Collectors.toSet());

		CarritoDTO carritoDTO = new CarritoDTO(carrito.getId(), carrito.getNombreCliente(),

				productosDTO);
		return ResponseEntity.ok(carritoDTO);
	}

	@DeleteMapping("/removeProducto/{id}")
	public ResponseEntity<Void> eliminarProductoDelCarrito(@PathVariable Long id) {
		logger.info("Eliminando producto del carrito con ID: " + id);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal().equals("anonymousUser")) {
			logger.warning("Cliente no autenticado");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		String username = authentication.getName();
		Cliente cliente = clienteService.obtenerPorUsername(username);
		if (cliente == null) {
			logger.warning("Cliente no encontrado para el usuario: " + username);
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		Producto producto = productoService.obtenerPorId(id).orElseThrow();
		if (producto == null) {
			logger.warning("Producto no encontrado con ID: " + id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Carrito carrito = obtenerOCrearCarrito(cliente);
		carrito.removeProducto(producto);
		clienteService.insertarCliente(cliente);
		logger.info("Producto eliminado del carrito exitosamente");
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/checkout/total")
	public ResponseEntity<Double> calcularTotalCarrito() {
		logger.info("Calculando el total del carrito");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal().equals("anonymousUser")) {
			logger.warning("Cliente no autenticado");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		String username = authentication.getName();
		Cliente cliente = clienteService.obtenerPorUsername(username);
		if (cliente == null) {
			logger.warning("Cliente no encontrado para el usuario: " + username);
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		Carrito carrito = obtenerOCrearCarrito(cliente);
		double total = carrito.getProductos().stream()
				.mapToDouble(cp -> cp.getProducto().getPrecio() * cp.getCantidad()).sum();

		logger.info("Total calculado: " + total);
		return new ResponseEntity<>(total, HttpStatus.OK);
	}

	@DeleteMapping("/clear")
	public ResponseEntity<Void> vaciarCarrito() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		String username = authentication.getName();
		Cliente cliente = clienteService.obtenerPorUsername(username);
		if (cliente == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Carrito carrito = cliente.getCarrito();
		carrito.getProductos().clear();
		clienteService.insertarCliente(cliente);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
