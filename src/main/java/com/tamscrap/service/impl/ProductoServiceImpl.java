package com.tamscrap.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tamscrap.model.Pedido;
import com.tamscrap.model.Producto;
import com.tamscrap.model.ProductosPedidos;
import com.tamscrap.repository.PedidoRepo;
import com.tamscrap.repository.ProductoRepo;
import com.tamscrap.service.ProductoService;

import jakarta.transaction.Transactional;

@Service
public class ProductoServiceImpl implements ProductoService {

	private final ProductoRepo productoRepo;
	private final PedidoRepo pedidoRepo;

	public ProductoServiceImpl(ProductoRepo productoRepo, PedidoRepo pedidoRepo) {
		this.productoRepo = productoRepo;
		this.pedidoRepo=pedidoRepo;
	}

	@Override
	public List<Producto> obtenerTodos() {
		return productoRepo.findAll();
	}

	@Override
	public Producto insertarProducto(Producto producto) {
		validarProducto(producto);
 		Producto savedProducto = productoRepo.save(producto);
 		return savedProducto;
	}

	private void validarProducto(Producto producto) {
		if (producto.getNombre() == null || producto.getNombre().isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
		}
		if (producto.getPrecio() <= 0) {
			throw new IllegalArgumentException("El precio debe ser mayor que cero");
		}
	}

	@Override
	public Producto obtenerPorId(Long id) {
		Optional<Producto> optionalProducto = productoRepo.findById(id);
		return optionalProducto.orElse(null);
	}

	@Override
	@Transactional
	public void eliminarProducto(Long id) {
	    // Buscar el producto por su ID
	    Producto producto = productoRepo.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));

	    // Eliminar referencias del producto en los pedidos
	    for (ProductosPedidos productosPedidos : producto.getPedidos()) {
	        Pedido pedido = productosPedidos.getPedido();
	        pedido.removeProducto(producto); // Eliminar el producto del pedido
 
	        if (pedido.getProductos().isEmpty()) {
	        	pedidoRepo.delete(pedido);
	        } else {
	        	pedidoRepo.save(pedido);  
	        }
	    }

	    // Finalmente, eliminar el producto de la base de datos
	    productoRepo.deleteById(id);
	}

	@Override
	public Producto obtenerPorNombre(String nombre) {
		Producto producto = productoRepo.findByNombre(nombre);
//		if (producto == null && !nombre.equals("TestProd2")) {
//			throw new RuntimeException("Producto no encontrado");
//		}
		return producto;
	}

	@Override
	public List<Producto> obtenerProductosPorCategoria(String categoria) {
		switch (categoria.toLowerCase()) {
		case "lettering":
			return productoRepo.findByLettering(true);
		case "scrapbooking":
			return productoRepo.findByScrapbooking(true);
		case "ofertas":
			return productoRepo.findByOferta(true);
		default:
			throw new IllegalArgumentException("Categoría no válida: " + categoria);
		}
	}

	@Override
	public List<Producto> buscarProductos(String term) {
		return productoRepo.findByNombreContainingIgnoreCase(term);
	}
	
}
