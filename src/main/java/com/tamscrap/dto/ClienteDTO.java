package com.tamscrap.dto;

import java.util.List;

import com.tamscrap.model.Carrito;

public class ClienteDTO {
	private Long id;
	private String username;
	private String nombre;
	private String email;
	private String password; // Agregado para registro y autenticación
	private List<String> authorities;
//	private Set<Producto> favoritos;
	private Carrito carrito;
	// Getters y Setters

	public Long getId() {
		return id;
	}

//	public Set<Producto> getFavoritos() {
//		return favoritos;
//	}
//
//	public void setFavoritos(Set<Producto> favoritos) {
//		this.favoritos = favoritos;
//	}

	public String getUsername() {
		return username;
	}

	public String getNombre() {
		return nombre;
	}

	public Carrito getCarrito() {
		return carrito;
	}

	public void setCarrito(Carrito carrito) {
		this.carrito = carrito;
	}

	public String getEmail() {
		return email;
	}

	public List<String> getAuthorities() {
		return authorities;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
