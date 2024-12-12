package com.tamscrap.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tamscrap.model.Cliente;
import com.tamscrap.model.Pedido;
import com.tamscrap.model.Producto;
import com.tamscrap.model.ProductoPedidoId;
import com.tamscrap.model.ProductosPedidos;
import com.tamscrap.repository.PedidoRepo;
import com.tamscrap.service.ClienteService;
import com.tamscrap.service.PedidoService;
import com.tamscrap.service.ProductoService;

import jakarta.transaction.Transactional;

@Service
public class PedidoServiceImpl implements PedidoService {

	private final PedidoRepo pedidoRepository;
	private final ProductoService productoService;
	private final ClienteService clienteService;

	@Autowired
	public PedidoServiceImpl(PedidoRepo pedidoRepository, ProductoService productoService,
			ClienteService clienteService) {
		this.pedidoRepository = pedidoRepository;
		this.productoService = productoService;
		this.clienteService = clienteService;
	}

	@Override
	public List<Pedido> obtenerTodos() {
		return pedidoRepository.findAll();
	}

	@Override
	public Pedido obtenerPorId(Long id) {
		return pedidoRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Pedido insertarPedido(Pedido pedido) {
		if (pedido.getCliente() == null || pedido.getCliente().getId() == null) {
			throw new IllegalArgumentException("El cliente asociado al pedido es inválido.");
		}

		if (pedido.getDireccionEnvio() == null || pedido.getDireccionEnvio().trim().isEmpty()) {
			throw new IllegalArgumentException("La dirección de envío es requerida.");
		}

		if (pedido.getMetodoPago() == null || pedido.getMetodoPago().trim().isEmpty()) {
			throw new IllegalArgumentException("El método de pago es requerido.");
		}

		// Para evitar problemas, cargamos el cliente completo desde BD (si no lo tienes
		// ya)
		Cliente cliente = clienteService.obtenerPorId(pedido.getCliente().getId());
		if (cliente == null) {
			throw new IllegalArgumentException("El cliente no existe en la base de datos.");
		}
		pedido.setCliente(cliente);

		// Guardamos el pedido sin productos primero para obtener su ID
		Set<ProductosPedidos> productosOriginales = new HashSet<>(pedido.getProductos());
		pedido.getProductos().clear(); // Limpia temporalmente la lista de productos
		pedido = pedidoRepository.save(pedido); // Ahora pedido tiene un ID

		Set<ProductosPedidos> productosFinales = new HashSet<>();

		for (ProductosPedidos ppOriginal : productosOriginales) {
			if (ppOriginal.getProducto() == null || ppOriginal.getProducto().getId() == null) {
				throw new IllegalArgumentException("Uno o más productos asociados al pedido son inválidos.");
			}

			// Cargar el producto completo
			Producto productoBD = productoService.obtenerPorId(ppOriginal.getProducto().getId());
			if (productoBD == null) {
				throw new IllegalArgumentException(
						"El producto con ID " + ppOriginal.getProducto().getId() + " no existe en la base de datos.");
			}

			int cantidadSolicitada = ppOriginal.getCantidad();
			if (productoBD.getCantidad() < cantidadSolicitada) {
				throw new IllegalArgumentException("Stock insuficiente para el producto: " + productoBD.getNombre());
			}

			// Ajustar stock
			productoBD.setCantidad(productoBD.getCantidad() - cantidadSolicitada);
			productoService.insertarProducto(productoBD);

			// Crear un nuevo ProductosPedidos con IDs establecidos
			ProductosPedidos nuevoPP = new ProductosPedidos();
			nuevoPP.setPedido(pedido);
			nuevoPP.setProducto(productoBD);
			nuevoPP.setCantidad(cantidadSolicitada);
			nuevoPP.setNombre(productoBD.getNombre());

			// Crear la clave compuesta manualmente ahora que ya tenemos pedido.getId() y
			// productoBD.getId()
			ProductoPedidoId ppId = new ProductoPedidoId(pedido.getId(), productoBD.getId());
			nuevoPP.setId(ppId);

			productosFinales.add(nuevoPP);
		}

		// Asignar la nueva lista de productos al pedido y recalcular precio
		pedido.setProductos(productosFinales);
		pedido.calcularPrecio();

		// Guardar el pedido con sus productos
		return pedidoRepository.save(pedido);
	}

	@Override
	public void eliminarPedido(Long id) {
		pedidoRepository.deleteById(id);
	}
}
