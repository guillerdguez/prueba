package com.tamscrap.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tamscrap.dto.ClienteDTO;
import com.tamscrap.dto.ProductoDTO;
import com.tamscrap.model.Cliente;
import com.tamscrap.model.UserAuthority;
import com.tamscrap.service.impl.ClienteServiceImpl;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = {"http://localhost:4200", "https://tamscraptpostreunion-production.up.railway.app"})
public class ClienteController {

	private final ClienteServiceImpl clienteService;

	public ClienteController(ClienteServiceImpl clienteService) {
		this.clienteService = clienteService;
	}

	// Obtener todos los clientes (solo ADMIN)
	@GetMapping("/listar")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<List<ClienteDTO>> obtenerTodosLosClientes() {
		List<ClienteDTO> clientes = clienteService.obtenerTodos().stream().map(this::convertirAClienteDTO)
				.collect(Collectors.toList());
		return new ResponseEntity<>(clientes, HttpStatus.OK);
	}

	// Obtener cliente por ID (solo ADMIN o el propio usuario)
	@GetMapping("/ver/{id}")
	@PreAuthorize("hasAuthority('ADMIN') or #id == principal.id")
	public ResponseEntity<ClienteDTO> obtenerClientePorId(@PathVariable Long id) {
		Cliente cliente = clienteService.obtenerPorId(id);
		if (cliente == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(convertirAClienteDTO(cliente), HttpStatus.OK);
	}

	// Actualizar cliente (solo ADMIN o el propio usuario)
	@PutMapping("/editar/{id}")
	@PreAuthorize("hasAuthority('ADMIN') or #id == principal.id")
	public ResponseEntity<ClienteDTO> editarCliente(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO) {
		Cliente clienteExistente = clienteService.obtenerPorId(id);
		if (clienteExistente == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		clienteExistente.setUsername(clienteDTO.getUsername());
		clienteExistente.setEmail(clienteDTO.getEmail());
		clienteExistente.setNombre(clienteDTO.getNombre());
//		clienteExistente.setFavoritos(clienteDTO.getFavoritos());
		// Si es ADMIN, puede cambiar los roles
		if (clienteDTO.getAuthorities() != null && !clienteDTO.getAuthorities().isEmpty()) {
			clienteExistente.setAuthorities(
					clienteDTO.getAuthorities().stream().map(UserAuthority::valueOf).collect(Collectors.toSet()));
		}

		clienteService.insertarCliente(clienteExistente);
		return new ResponseEntity<>(convertirAClienteDTO(clienteExistente), HttpStatus.OK);
	}

	// Eliminar cliente (solo ADMIN)
	@DeleteMapping("/borrar/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
		clienteService.eliminarCliente(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	private ClienteDTO convertirAClienteDTO(Cliente cliente) {
		ClienteDTO dto = new ClienteDTO();
		dto.setId(cliente.getId());
		dto.setUsername(cliente.getUsername());
		dto.setNombre(cliente.getNombre());
		dto.setEmail(cliente.getEmail());
//		dto.setFavoritos(cliente.getFavoritos());
		dto.setAuthorities(
				cliente.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
		return dto;
	}
	// Agregar producto a favoritos
	@PostMapping("/{clienteId}/favorito/{productoId}")
 	public ResponseEntity<ClienteDTO> agregarAFavoritos(@PathVariable Long clienteId, @PathVariable Long productoId) {
	Cliente  cliente=  clienteService.agregarAFavoritos(clienteId, productoId);
	    
//	    return new ResponseEntity<>(HttpStatus.CREATED).bo;añadir url del get del producto
	    return ResponseEntity.created(null).body(convertirAClienteDTO(cliente));
	}

	// Eliminar producto de favoritos
	@DeleteMapping("/{clienteId}/favorito/{productoId}")
 	public ResponseEntity<Void> eliminarDeFavoritos(@PathVariable Long clienteId, @PathVariable Long productoId) {
	    clienteService.agregarAFavoritos(clienteId, productoId);
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// Obtener la lista de favoritos del cliente
	@GetMapping("/{clienteId}/favoritos")
 	public ResponseEntity<List<ProductoDTO>> obtenerFavoritos(@PathVariable Long clienteId) {
	    List<ProductoDTO> favoritos = clienteService.obtenerFavoritos(clienteId);
	    return new ResponseEntity<>(favoritos, HttpStatus.OK);
	}

}
