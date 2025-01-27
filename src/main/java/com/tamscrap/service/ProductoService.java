package com.tamscrap.service;

import java.util.List;
import java.util.Optional;

import com.tamscrap.dto.ProductoDTO;
import com.tamscrap.model.Producto;

public interface ProductoService {
	 Producto   obtenerEntidadPorId(Long id);

	ProductoDTO obtenerDtoPorId(Long id);

	ProductoDTO obtenerPorNombre(String nombre);

	Producto  insertarProducto(Producto producto);

	ProductoDTO editarProducto(Long id, ProductoDTO productoDTO);

	void eliminarProducto(Long id);

	List<ProductoDTO> obtenerTodos();

	List<ProductoDTO> obtenerProductosPorCategoria(String categoria);

	List<ProductoDTO> buscarProductos(String term);

	Optional<Producto> obtenerPorId(Long id);
}
