package com.tamscrap.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalIdCache;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "PRODUCTOS")
@NaturalIdCache
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Producto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "nombre", unique = true, nullable = true)
	private String nombre;

	@Column(name = "precio")
	private double precio;

	@Column(name = "imagen")
	private String imagen;

	@Column(name = "lettering")
	private boolean lettering;

	@Column(name = "scrapbooking")
	private boolean scrapbooking;

	@Column(name = "oferta")
	private boolean oferta;

	@Column(name = "descuento")
	private Integer descuento;

	@Column(name = "cantidad")
	private int cantidad;

	@Column(name = "precio_original", nullable = true)
	private Double precioOriginal;

	@OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductosPedidos> pedidos = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "carrito_id")
	private Carrito carrito;

	public Producto() {
	}

	public Producto(Long id, String nombre, double precio, String imagen, boolean lettering, boolean scrapbooking,
			boolean oferta, Integer descuento, int cantidad, boolean favorito, Double precioOriginal) {

		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.imagen = imagen;
		this.lettering = lettering;
		this.scrapbooking = scrapbooking;
		this.oferta = oferta;
		this.descuento = descuento;
		this.cantidad = cantidad;

		this.precioOriginal = precioOriginal;
	}

	public Producto(Long id, String nombre, double precio, String imagen, boolean lettering, boolean scrapbooking,
			boolean oferta, Integer descuento, boolean favorito, Double precioOriginal, Set<ProductosPedidos> pedidos) {

		this.id = id;
		this.precio = precio;
		this.imagen = imagen;
		this.lettering = lettering;
		this.scrapbooking = scrapbooking;
		this.oferta = oferta;
		this.descuento = descuento;

		this.precioOriginal = precioOriginal;
		this.pedidos = pedidos;
	}

	@Override
	public String toString() {
		return "Producto [id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", imagen=" + imagen
				+ ", lettering=" + lettering + ", scrapbooking=" + scrapbooking + ", oferta=" + oferta + ", descuento="
				+ descuento + ", cantidad=" + cantidad + ", precioOriginal=" + precioOriginal + "]";
	}

	// Getters y Setters para todos los campos, incluyendo precioOriginal

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public boolean isLettering() {
		return lettering;
	}

	public void setLettering(boolean lettering) {
		this.lettering = lettering;
	}

	public boolean isScrapbooking() {
		return scrapbooking;
	}

	public void setScrapbooking(boolean scrapbooking) {
		this.scrapbooking = scrapbooking;
	}

	public boolean isOferta() {
		return oferta;
	}

	public void setOferta(boolean oferta) {
		this.oferta = oferta;
	}

	public Integer getDescuento() {
		return descuento;
	}

	public void setDescuento(Integer descuento) {
		this.descuento = descuento;
	}

//    public Double getPrecioOriginal() {
//        if (this.oferta && this.descuento != null && this.descuento > 0) {
//            return this.precio / (1 - this.descuento / 100.0);
//        }
//        return null; // No está en oferta, no hay precio original
//    }
	public Double getPrecioOriginal() {
		if (precioOriginal != null) {
			return precioOriginal;
		}
		if (descuento == null || descuento <= 0) {
			return null;
		}
		return precio / (1 - (descuento / 100.0));
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public void desactivarOferta() {
		if (oferta) {
			precio = precioOriginal; // Restablece el precio original
			descuento = null;
			oferta = false;
			precioOriginal = null;
		}
	}

	public void setPrecioOriginal(Double precioOriginal) {
		this.precioOriginal = precioOriginal;
	}

	public Set<ProductosPedidos> getPedidos() {
		return pedidos;
	}

	public void setPedidos(Set<ProductosPedidos> pedidos) {
		this.pedidos = pedidos;
	}

	public Carrito getCarrito() {
		return carrito;
	}

	public void setCarrito(Carrito carrito) {
		this.carrito = carrito;
	}

	// Método para calcular el precio final basado en el descuento
	public double getPrecioFinal() {
		if (oferta && descuento != null && descuento > 0) {
			return precio - (precio * descuento / 100.0);
		}
		return precio;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cantidad, descuento, id, imagen, lettering, nombre, oferta, precio, precioOriginal,
				scrapbooking);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Producto other = (Producto) obj;
		return cantidad == other.cantidad && Objects.equals(descuento, other.descuento) && Objects.equals(id, other.id)
				&& Objects.equals(imagen, other.imagen) && lettering == other.lettering
				&& Objects.equals(nombre, other.nombre) && oferta == other.oferta
				&& Double.doubleToLongBits(precio) == Double.doubleToLongBits(other.precio)
				&& Objects.equals(precioOriginal, other.precioOriginal) && scrapbooking == other.scrapbooking;
	}

}
