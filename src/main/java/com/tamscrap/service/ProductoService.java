package com.tamscrap.service;

import java.util.List;

import com.tamscrap.model.Producto;

public interface ProductoService {

	Producto obtenerPorId(Long id);

	Producto obtenerPorNombre(String username);

	Producto insertarProducto(Producto Producto);

	void eliminarProducto(Long id);

	List<Producto> obtenerTodos();

}
