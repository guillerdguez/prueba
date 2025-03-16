package com.tamscrap.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class PedidoDTOListar {
	private Long id;
	private double precio;
	private LocalDateTime fechaCreacion;
	private ClienteDTOListarPedidos cliente;
	private Set<ProductoPedidoDTO> productos;
	private String direccionEnvio;
	private String metodoPago;
	private String estado;
	private String nombreComprador;

	public PedidoDTOListar() {
		productos = new HashSet<>();
	}

	public String getNombreComprador() {
		return nombreComprador;
	}

	public void setNombreComprador(String nombreComprador) {
		this.nombreComprador = nombreComprador;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public ClienteDTOListarPedidos getCliente() {
		return cliente;
	}

	public void setCliente(ClienteDTOListarPedidos cliente) {
		this.cliente = cliente;
	}

	public Set<ProductoPedidoDTO> getProductos() {
		return productos;
	}

	public void setProductos(Set<ProductoPedidoDTO> productos) {
		this.productos = productos;
	}

	public String getDireccionEnvio() {
		return direccionEnvio;
	}

	public void setDireccionEnvio(String direccionEnvio) {
		this.direccionEnvio = direccionEnvio;
	}

	public String getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
