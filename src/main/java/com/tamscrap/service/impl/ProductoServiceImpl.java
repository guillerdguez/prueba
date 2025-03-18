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

import jakarta.persistence.EntityExistsException;
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
	    
 	    if (productoRepo.findByNombre(producto.getNombre()) != null) {
	        throw new EntityExistsException("Ya existe un producto con el nombre: " + producto.getNombre());
	    }
	    
	    return productoRepo.save(producto);
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
		Producto producto = productoRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));

		for (ProductosPedidos productosPedidos : producto.getPedidos()) {
			Pedido pedido = productosPedidos.getPedido();
			pedido.removeProducto(producto);

			if (pedido.getProductos().isEmpty()) {
				pedidoRepo.delete(pedido);
			} else {
				pedidoRepo.save(pedido);
			}
		}

		productoRepo.deleteById(id);
	}

	@Override
	public ProductoDTO obtenerPorNombre(String nombre) {
		ProductoDTO productoDTO = productoRepo.findByNombreDto(nombre);
		return productoDTO;
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
	public Producto  obtenerPorNombreNoDto(String nombre) {
	    Producto  producto  = productoRepo.findByNombre (nombre);  
	    return producto ;
	}
	@Override
	public List<ProductoDTO> buscarProductos(String term) {
		return productoRepo.findByNombreContainingIgnoreCase(term);
	}

	@Override
	public ProductoDTO editarProducto(Long id, ProductoDTO productoDTO) {
		Producto productoExistente = productoRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("No se encontró el producto con ID: " + id));

		productoExistente.setNombre(productoDTO.getNombre());
		productoExistente.setPrecio(productoDTO.getPrecio());
		productoExistente.setImagen(productoDTO.getImagen());
		productoExistente.setLettering(productoDTO.isLettering());
		productoExistente.setScrapbooking(productoDTO.isScrapbooking());
		productoExistente.setOferta(productoDTO.isOferta());
		productoExistente.setDescuento(productoDTO.getDescuento());
		productoExistente.setCantidad(productoDTO.getCantidad());
		productoExistente.setDescripcion(productoDTO.getDescripcion());

		validarProducto(productoExistente);

		Producto productoGuardado = productoRepo.save(productoExistente);
		return new ProductoDTO(productoGuardado);
	}

	private void validarProducto(Producto producto) {
		if (producto.getNombre() == null || producto.getNombre().isEmpty()) {
			throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
		}
		if (producto.getPrecio() <= 0) {
			throw new IllegalArgumentException("El precio debe ser mayor que cero");
		}

	}
}
