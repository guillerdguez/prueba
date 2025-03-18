package com.tamscrap.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tamscrap.dto.ProductoDTO;
import com.tamscrap.model.Cliente;
import com.tamscrap.model.Producto;
import com.tamscrap.repository.ClienteRepo;
import com.tamscrap.service.ClienteService;
import com.tamscrap.service.ProductoService;

@Service
public class ClienteServiceImpl implements ClienteService {

	private final ClienteRepo clienteRepository;
	private final ProductoService productoRepository;

	public ClienteServiceImpl(ClienteRepo clienteRepository, ProductoService productoRepository) {
		this.clienteRepository = clienteRepository;
		this.productoRepository = productoRepository;
	}

	@Override
	public Cliente insertarCliente(Cliente cliente) {
	    if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
	        throw new IllegalArgumentException("El nombre del cliente no puede ser nulo o vacío");
	    }
	    if (cliente.getUsername() == null || cliente.getUsername().trim().isEmpty()) {
	        throw new IllegalArgumentException("El username no puede ser nulo o vacío");
	    }
	    return clienteRepository.save(cliente);
	}
	@Override
	public List<Cliente> obtenerTodos() {
		return clienteRepository.findAll();
	}

	@Override
	public Cliente obtenerPorId(Long id) {
		return clienteRepository.findById(id).orElse(null);
	}

	@Override
	public void eliminarCliente(Long id) {
		clienteRepository.deleteById(id);
	}

	@Override
	public Cliente obtenerPorUsername(String username) {
		return clienteRepository.findByUsername(username).orElse(null);
	}

	@Override
	public Cliente agregarAFavoritos(Long clienteId, Long productoId) {
		Cliente cliente = clienteRepository.findById(clienteId)
				.orElseThrow(() -> new RuntimeException("Cliente no encontrado."));
		Producto producto = productoRepository.obtenerPorId(productoId)
				.orElseThrow(() -> new RuntimeException("Producto no encontrado."));
		if (!cliente.getFavoritos().contains(producto)) {
			cliente.addFavorito(producto);
		} else {
			cliente.removeFavorito(producto);
		}
		return clienteRepository.save(cliente);

	}

	@Override
	public List<ProductoDTO> obtenerFavoritos(Long clienteId) {
		Cliente cliente = clienteRepository.findById(clienteId)
				.orElseThrow(() -> new RuntimeException("Cliente no encontrado."));

		return cliente.getFavoritos().stream().map(producto -> new ProductoDTO(producto))

				.collect(Collectors.toList());
	}

}
