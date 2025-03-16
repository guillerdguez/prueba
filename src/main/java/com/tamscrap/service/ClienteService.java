package com.tamscrap.service;

import java.util.List;

import com.tamscrap.dto.ProductoDTO;
import com.tamscrap.model.Cliente;

public interface ClienteService {

	Cliente insertarCliente(Cliente cliente);

	List<Cliente> obtenerTodos();

	Cliente obtenerPorId(Long id);

	void eliminarCliente(Long id);

	Cliente obtenerPorUsername(String username);

	Cliente agregarAFavoritos(Long clienteId, Long productoId);

	List<ProductoDTO> obtenerFavoritos(Long clienteId);
}
