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
	public List<Pedido> obtenerPorClienteId(Long clienteId) {
	    return pedidoRepository.findByClienteId(clienteId);
	}

	@Override
	public Pedido obtenerPorId(Long id) {
		return pedidoRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Pedido insertarPedido(Pedido pedido) {
	    // Validaciones básicas del pedido
	    if (pedido.getCliente() == null || pedido.getCliente().getId() == null ||
	        pedido.getDireccionEnvio() == null || pedido.getDireccionEnvio().trim().isEmpty() ||
	        pedido.getMetodoPago() == null || pedido.getMetodoPago().trim().isEmpty()) {
	        throw new IllegalArgumentException("Datos del pedido inválidos (cliente, dirección o método de pago).");
	    }

	    // Cargar cliente
	    Cliente cliente = clienteService.obtenerPorId(pedido.getCliente().getId());
	    if (cliente == null) {
	        throw new IllegalArgumentException("El cliente no existe en la base de datos.");
	    }
	    pedido.setCliente(cliente);

	    // Guardar pedido sin productos para obtener su ID
	    Set<ProductosPedidos> productosOriginales = new HashSet<>(pedido.getProductos());
	    pedido.getProductos().clear();
	    pedido = pedidoRepository.save(pedido);

	    Set<ProductosPedidos> productosFinales = new HashSet<>();

	    // Validar stock y procesar productos
	    for (ProductosPedidos ppOriginal : productosOriginales) {
	        if (ppOriginal.getProducto() == null || ppOriginal.getProducto().getId() == null) {
	            throw new IllegalArgumentException("Uno o más productos asociados al pedido son inválidos.");
	        }

	        Producto productoBD = productoService.obtenerPorId(ppOriginal.getProducto().getId());
	        if (productoBD == null) {
	            throw new IllegalArgumentException(
	                "El producto con ID " + ppOriginal.getProducto().getId() + " no existe en la base de datos."
	            );
	        }

	        int cantidadSolicitada = ppOriginal.getCantidad();
	        if (productoBD.getCantidad() < cantidadSolicitada) {
	            throw new IllegalArgumentException(
	                "Stock insuficiente para el producto: " + productoBD.getNombre()
	            );
	        }

	        // Ajustar stock
	        productoBD.setCantidad(productoBD.getCantidad() - cantidadSolicitada);
	        productoService.insertarProducto(productoBD);

	        // Crear ProductosPedidos con la clave compuesta
	        ProductosPedidos nuevoPP = new ProductosPedidos();
	        nuevoPP.setPedido(pedido);
	        nuevoPP.setProducto(productoBD);
	        nuevoPP.setCantidad(cantidadSolicitada);
	        nuevoPP.setNombre(productoBD.getNombre());
	        nuevoPP.setId(new ProductoPedidoId(pedido.getId(), productoBD.getId()));

	        productosFinales.add(nuevoPP);
	    }

	    // Asociar productos, recalcular precio y guardar
	    pedido.setProductos(productosFinales);
	    pedido.calcularPrecio();
	    return pedidoRepository.save(pedido);
	}

	@Override
	@Transactional
	public void eliminarPedido(Long id) {
	    // Buscar el pedido en la base de datos
	    Pedido pedido = pedidoRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con el ID: " + id));

	    // Limpiar referencias en el cliente
	    if (pedido.getCliente() != null) {
	        pedido.getCliente().getPedidos().remove(pedido);
	    }

	    // Limpiar referencias en productos
	    for (ProductosPedidos productoPedido : pedido.getProductos()) {
	        Producto producto = productoPedido.getProducto();
	        if (producto != null) {
	            // Restaurar el stock del producto
	            producto.setCantidad(producto.getCantidad() + productoPedido.getCantidad());
	            producto.getPedidos().remove(productoPedido); // Limpiar la relación inversa
	        }
	    }

	    // Eliminar el pedido
	    pedidoRepository.delete(pedido);
	}

}
