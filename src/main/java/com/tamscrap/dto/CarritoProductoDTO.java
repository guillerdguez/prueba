package com.tamscrap.dto;

public class CarritoProductoDTO {
	private Long id;
	private int cantidad;
	private ProductoDTO producto;

	public CarritoProductoDTO() {
	}

	public Long getProductoId() {
		return id;
	}

	public CarritoProductoDTO(Long id, ProductoDTO producto, int cantidad) {
		this.id = id;
		this.producto = producto;
		this.cantidad = cantidad;
	}

	public void setProductoId(Long productoId) {
		this.id = productoId;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public ProductoDTO getProducto() {
		return producto;
	}

	public void setProducto(ProductoDTO producto) {
		this.producto = producto;
	}
}
