package com.tamscrap.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tamscrap.dto.ProductoDTO;
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
		this.pedidoRepo = pedidoRepo;
	}

	@Override
	public List<ProductoDTO> obtenerTodos() {
		return productoRepo.findAllAsDTO();
	};

	@Override
	public Producto insertarProducto(Producto producto) {

		validarProducto(producto);
		Producto savedProducto = productoRepo.save(producto);
		return savedProducto;
	}

	@Override
	public ProductoDTO obtenerDtoPorId(Long id) {
		Optional<Producto> optionalProducto = productoRepo.findById(id);
		return optionalProducto.map(ProductoDTO::new).orElse(null);
	}

	@Override
	public Producto obtenerEntidadPorId(Long id) {
		return (Producto) productoRepo.findById(id).orElse(null);
	}

	@Override
	public Optional<Producto> obtenerPorId(Long id) {
		Optional<Producto> optionalProducto = productoRepo.findById(id);
		return optionalProducto;
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
	public ProductoDTO obtenerPorNombre(String nombre) {
		ProductoDTO productoDTO = productoRepo.findByNombre(nombre);
		return productoDTO; // Puede retornar null si no existe
	}

	@Override
	public List<ProductoDTO> obtenerProductosPorCategoria(String categoria) {
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
	public List<ProductoDTO> buscarProductos(String term) {
		return productoRepo.findByNombreContainingIgnoreCase(term);
	}

	@Override
	public ProductoDTO editarProducto(Long id, ProductoDTO productoDTO) {
		// Verificamos si existe el producto en la BD
		Producto productoExistente = productoRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("No se encontró el producto con ID: " + id));

		// Actualizamos los campos necesarios
		productoExistente.setNombre(productoDTO.getNombre());
		productoExistente.setPrecio(productoDTO.getPrecio());
		productoExistente.setImagen(productoDTO.getImagen());
		productoExistente.setLettering(productoDTO.isLettering());
		productoExistente.setScrapbooking(productoDTO.isScrapbooking());
		productoExistente.setOferta(productoDTO.isOferta());
		productoExistente.setDescuento(productoDTO.getDescuento());
		productoExistente.setCantidad(productoDTO.getCantidad());
		productoExistente.setDescripcion(productoDTO.getDescripcion());
		// Podríamos recalcular precioOriginal si es oferta y descuento > 0, etc.

		validarProducto(productoExistente);

		// Guardamos los cambios
		Producto productoGuardado = productoRepo.save(productoExistente);
		return new ProductoDTO(productoGuardado);
	}

	// ---------------------------------------------------
	// Métodos auxiliares
	// ---------------------------------------------------

	private void validarProducto(Producto producto) {
		if (producto.getNombre() == null || producto.getNombre().isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
		}
		if (producto.getPrecio() <= 0) {
			throw new IllegalArgumentException("El precio debe ser mayor que cero");
		}
//	}
//
//	private Producto convertirADominio(ProductoDTO productoDTO) {
//		Producto producto = new Producto();
//		producto.setId(productoDTO.getId());
//		producto.setNombre(productoDTO.getNombre());
//		producto.setPrecio(productoDTO.getPrecio());
//		producto.setImagen(productoDTO.getImagen());
//		producto.setLettering(productoDTO.isLettering());
//		producto.setScrapbooking(productoDTO.isScrapbooking());
//		producto.setOferta(productoDTO.isOferta());
//		producto.setDescuento(productoDTO.getDescuento());
//		producto.setCantidad(productoDTO.getCantidad());
//		producto.setDescripcion(productoDTO.getDescripcion());
//		// Si hay lógica adicional para precioOriginal, puedes manejarla aquí o en
//		// setOferta...
//		return producto;
 	}
}
