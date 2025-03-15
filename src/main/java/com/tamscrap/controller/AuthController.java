package com.tamscrap.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tamscrap.config.JwtTokenUtil;
import com.tamscrap.dto.ClienteDTO;
import com.tamscrap.model.Cliente;
import com.tamscrap.model.UserAuthority;
import com.tamscrap.repository.ClienteRepo;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:4200", "https://tamscrapt.up.railway.app"})
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ClienteRepo clienteRepo;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody ClienteDTO clienteDTO) {
		try {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(clienteDTO.getUsername(), clienteDTO.getPassword());
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String token = jwtTokenUtil.generateToken(userDetails);
			return ResponseEntity.ok(new AuthResponse(token, convertirAClienteDTO((Cliente) userDetails)));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
		}
	}

	@PostMapping("/register")
	public ResponseEntity<Map<String, String>> register(@RequestBody ClienteDTO clienteDTO) {
		if (clienteRepo.findByUsername(clienteDTO.getUsername()).isPresent()) {
			Map<String, String> response = new HashMap<>();
			response.put("message", "El username ya existe");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		if (clienteRepo.findByEmail(clienteDTO.getEmail()).isPresent()) {
			Map<String, String> response = new HashMap<>();
			response.put("message", "El email ya está registrado");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		Cliente cliente = convertirACliente(clienteDTO);
		cliente.setPassword(passwordEncoder.encode(clienteDTO.getPassword()));
		cliente.setAuthorities(clienteDTO.getAuthorities().stream().map(UserAuthority::valueOf).collect(Collectors.toSet()));
		clienteRepo.save(cliente);
		Map<String, String> response = new HashMap<>();
		response.put("message", "Usuario registrado exitosamente");
		return ResponseEntity.ok(response);
	}

	private ClienteDTO convertirAClienteDTO(Cliente cliente) {
		ClienteDTO dto = new ClienteDTO();
		dto.setId(cliente.getId());
		dto.setUsername(cliente.getUsername());
		dto.setNombre(cliente.getNombre());
		dto.setEmail(cliente.getEmail());
		dto.setAuthorities(cliente.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
		return dto;
	}

	private Cliente convertirACliente(ClienteDTO dto) {
		Cliente cliente = new Cliente();
		cliente.setUsername(dto.getUsername());
		cliente.setNombre(dto.getNombre());
		cliente.setEmail(dto.getEmail());
		return cliente;
	}

	public static class AuthResponse {
		private String token;
		private ClienteDTO user;

		public AuthResponse(String token, ClienteDTO user) {
			this.token = token;
			this.user = user;
		}

		public String getToken() {
			return token;
		}

		public ClienteDTO getUser() {
			return user;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public void setUser(ClienteDTO user) {
			this.user = user;
		}
	}
}
