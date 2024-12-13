package com.tamscrap.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tamscrap.dto.PedidoUpdateRequest;
import com.tamscrap.model.Cliente;
import com.tamscrap.model.Pedido;
import com.tamscrap.model.Producto;
import com.tamscrap.model.ProductosPedidos;
import com.tamscrap.service.impl.ClienteServiceImpl;
import com.tamscrap.service.impl.PedidoServiceImpl;
import com.tamscrap.service.impl.ProductoServiceImpl;
import com.tamscrap.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "http://localhost:4200/")
public class PedidoController {

	private final ProductoServiceImpl productoService;
	private final PedidoServiceImpl pedidoService;
	private final ClienteServiceImpl clienteService;
	private static final Logger logger = Logger.getLogger(PedidoController.class.getName());

	public PedidoController(ProductoServiceImpl productoService, PedidoServiceImpl pedidoService,
			ClienteServiceImpl clienteService) {
		this.productoService = productoService;
		this.pedidoService = pedidoService;
		this.clienteService = clienteService;
	}

	@PostMapping("/addPedido")
	public ResponseEntity<Void> guardarPedido(@RequestBody Pedido pedido, @AuthenticationPrincipal Cliente cliente) {

		Long clienteId = cliente.getId();

		logger.log(Level.INFO, "Pedido recibido: {0}", pedido);
		System.err.println(pedido.getCliente() + "Pedidodddddd");
		// Validaciones básicas antes de delegar al servicio
		if (pedido.getDireccionEnvio() == null || pedido.getDireccionEnvio().trim().isEmpty()) {
			logger.log(Level.WARNING, "La dirección de envío es requerida.");
			return ResponseEntity.badRequest().build();
		}

		if (pedido.getMetodoPago() == null || pedido.getMetodoPago().trim().isEmpty()) {
			logger.log(Level.WARNING, "El método de pago es requerido.");
			return ResponseEntity.badRequest().build();
		}

		try {
		 pedido.setCliente(clienteService.obtenerPorId(clienteId));
			pedidoService.insertarPedido(pedido);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (IllegalArgumentException e) {
			logger.log(Level.WARNING, "Error al crear el pedido: {0}", e.getMessage());
			return ResponseEntity.badRequest().build();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error interno al crear el pedido", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// READ
	@GetMapping("/listar")
	public ResponseEntity<List<Pedido>> mostrarPedidos() {
		logger.log(Level.INFO, "Obteniendo todos los pedidos");
		List<Pedido> pedidos = pedidoService.obtenerTodos();
		return new ResponseEntity<>(pedidos, HttpStatus.OK);
	}

	@GetMapping("/ver/{id}")
	public ResponseEntity<?> obtenerPedido(@PathVariable Long id) {
		logger.log(Level.INFO, "Obteniendo pedido con ID: {0}", id);
		Pedido pedido = pedidoService.obtenerPorId(id);
		if (pedido == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(pedido);
	}

	// UPDATE
	@PutMapping("/editar/{id}")
	public ResponseEntity<?> actualizarPedido(@PathVariable Long id, @RequestBody PedidoUpdateRequest pedidoUpdate) {
		logger.log(Level.INFO, "Actualizando pedido con ID: {0}", id);

		Pedido pedido = pedidoUpdate.getPedido();
		List<Integer> cantidades = pedidoUpdate.getCantidades();

		Pedido pedidoExistente = pedidoService.obtenerPorId(id);
		if (pedidoExistente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Cliente cliente = clienteService.obtenerPorId(pedido.getCliente().getId());
		if (cliente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		pedidoExistente.setCliente(cliente);
		pedidoExistente.setDireccionEnvio(pedido.getDireccionEnvio());
		pedidoExistente.setMetodoPago(pedido.getMetodoPago());

		Set<ProductosPedidos> productosPedidos = pedidoExistente.getProductos();
		ProductosPedidos[] productosArray = productosPedidos.toArray(new ProductosPedidos[0]);

		for (int i = 0; i < Math.min(productosArray.length, cantidades.size()); i++) {
			ProductosPedidos productoPedido = productosArray[i];
			if (cantidades.get(i) > 0) {
				productoPedido.setCantidad(cantidades.get(i));
			} else {
				pedidoExistente.removeProducto(productoPedido.getProducto());
			}
		}

		try {
			pedidoService.insertarPedido(pedidoExistente);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error al actualizar el pedido", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// ADD PRODUCT
	@PostMapping("/addProducto/{id}")
	public ResponseEntity<?> agregarProducto(@PathVariable Long id, @RequestParam("idProducto") Long idProducto,
			@RequestParam("cantidad") int cantidad) {
		logger.log(Level.INFO, "Agregando producto con ID {0} al pedido con ID {1}", new Object[] { idProducto, id });

		Pedido pedidoExistente = pedidoService.obtenerPorId(id);
		if (pedidoExistente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Producto producto = productoService.obtenerPorId(idProducto);
		if (producto == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		pedidoExistente.addProducto2(producto, cantidad);
		pedidoExistente.calcularPrecio();

		try {
			Pedido updatedPedido = pedidoService.insertarPedido(pedidoExistente);
			return ResponseEntity.ok(updatedPedido);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// REMOVE PRODUCT
	@PostMapping("/removeProducto")
	public ResponseEntity<?> removeProducto(@RequestBody Map<String, Long> request) {
		Long pedidoId = request.get("pedidoId");
		Long productoId = request.get("productoId");

		logger.log(Level.INFO, "Eliminando producto con ID {0} del pedido con ID {1}",
				new Object[] { productoId, pedidoId });

//		if (pedidoId == null || productoId == null) {
//			return ResponseEntity.badRequest().body("El ID del pedido y el producto son obligatorios.");
//		}

		Pedido pedido = pedidoService.obtenerPorId(pedidoId);
		if (pedido == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Producto producto = productoService.obtenerPorId(productoId);
		if (producto == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		pedido.removeProducto(producto);
		try {
			pedidoService.insertarPedido(pedido);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error al eliminar producto del pedido", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// DELETE
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> eliminarPedido(@PathVariable Long id) {
		logger.log(Level.INFO, "Eliminando pedido con ID: {0}", id);
		pedidoService.eliminarPedido(id);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
