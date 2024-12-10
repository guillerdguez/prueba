package com.tamscrap.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tamscrap.model.Cliente;
import com.tamscrap.model.Producto;
import com.tamscrap.repository.ClienteRepo;
import com.tamscrap.repository.ProductoRepo;
import com.tamscrap.service.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService {

	private final ClienteRepo clienteRepository;
	private final ProductoRepo productoRepository;

	public ClienteServiceImpl(ClienteRepo clienteRepository, ProductoRepo productoRepository) {
		this.clienteRepository = clienteRepository;
		this.productoRepository = productoRepository;
	}

	@Override
	public Cliente insertarCliente(Cliente cliente) {
		if (cliente.getUsername() == null || cliente.getUsername().isEmpty()) {
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

	// --- Métodos para gestión de favoritos ---

	@Override
//    @Transactional
	public void agregarAFavoritos(Long clienteId, Long productoId) {
		Cliente cliente = clienteRepository.findById(clienteId)
				.orElseThrow(() -> new RuntimeException("Cliente no encontrado."));
		Producto producto = productoRepository.findById(productoId)
				.orElseThrow(() -> new RuntimeException("Producto no encontrado."));

		if (!cliente.getFavoritos().contains(producto)) {
			cliente.addFavorito(producto);
			clienteRepository.save(cliente); // Guardar cambios
		}
	}

	@Override
//    @Transactional
	public void eliminarDeFavoritos(Long clienteId, Long productoId) {
		Cliente cliente = clienteRepository.findById(clienteId)
				.orElseThrow(() -> new RuntimeException("Cliente no encontrado."));
		Producto producto = productoRepository.findById(productoId)
				.orElseThrow(() -> new RuntimeException("Producto no encontrado."));

		if (cliente.getFavoritos().contains(producto)) {
			cliente.removeFavorito(producto);
			System.out.println("favoritoss"+"   "+cliente.getFavoritos());
			clienteRepository.save(cliente); // Guardar cambios
		}
	}

	@Override
//    @Transactional(readOnly = true)
	public List<Producto> obtenerFavoritos(Long clienteId) {
		Cliente cliente = clienteRepository.findById(clienteId)
				.orElseThrow(() -> new RuntimeException("Cliente no encontrado."));
		return List.copyOf(cliente.getFavoritos());
	}
}
