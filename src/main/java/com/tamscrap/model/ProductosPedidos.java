package com.tamscrap.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "PRODUCTOS_PEDIDOS")
public class ProductosPedidos {

	@EmbeddedId
	private ProductoPedidoId id;

	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("pedidoId")
	@JsonBackReference
	private Pedido pedido;

	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("productoId")
	private Producto producto;

	@Column(name = "CANTIDAD")
	private int cantidad = 0;

	@Column(name = "NOMBRE")
	private String nombre;

	public ProductosPedidos() {
	}

	public ProductosPedidos(Producto producto, Pedido pedido, int cantidad) {
		this.producto = producto;
		this.pedido = pedido;
		this.cantidad = cantidad;
		this.nombre = producto.getNombre();
		this.id = new ProductoPedidoId(pedido.getId(), producto.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductosPedidos other = (ProductosPedidos) obj;
		return Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre)
				&& Objects.equals(producto, other.producto);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nombre, producto);
	}

	@Override
	public String toString() {
		return "ProductosPedidos{" + "id=" + id + ", pedido=" + pedido + ", producto=" + producto + ", cantidad="
				+ cantidad + ", nombre='" + nombre + '\'' + '}';
	}

	public ProductoPedidoId getId() {
		return id;
	}

	public void setId(ProductoPedidoId id) {
		this.id = id;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
