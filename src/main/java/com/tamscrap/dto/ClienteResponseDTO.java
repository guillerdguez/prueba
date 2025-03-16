package com.tamscrap.dto;

import java.util.List;
import java.util.Set;

public class ClienteResponseDTO {
	private Long id;
	private String username;
	private String nombre;
	private String email;
	private List<String> authorities;
	private Set<ProductoDTO> favoritos;
	private CarritoDTO carrito;

	public ClienteResponseDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

	public Set<ProductoDTO> getFavoritos() {
		return favoritos;
	}

	public void setFavoritos(Set<ProductoDTO> favoritos) {
		this.favoritos = favoritos;
	}

	public CarritoDTO getCarrito() {
		return carrito;
	}

	public void setCarrito(CarritoDTO carrito) {
		this.carrito = carrito;
	}
}
