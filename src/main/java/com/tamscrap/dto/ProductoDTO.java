package com.tamscrap.dto;

public class ProductoDTO {

	private Long id;
	private String nombre;
	private double precio;
	private String imagen;
	private boolean lettering;
	private boolean scrapbooking;
	private boolean oferta;
	private Integer descuento;
	private Double precioOriginal;
	private int cantidad;
	private String descripcion;

	public ProductoDTO() {
	}

	public ProductoDTO(Long id, String nombre, Double precio, String imagen, int cantidad) {
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.imagen = imagen;
		this.cantidad = cantidad;
	}

	public ProductoDTO(Long id, String nombre, Double precio) {
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
	}

	public ProductoDTO(Long id, String nombre, double precio, String imagen, boolean lettering, boolean scrapbooking,
			boolean oferta, Integer descuento, Double precioOriginal, int cantidad, String descripcion) {
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.imagen = imagen;
		this.lettering = lettering;
		this.scrapbooking = scrapbooking;
		this.oferta = oferta;
		this.descuento = descuento;
		this.precioOriginal = precioOriginal;
		this.cantidad = cantidad;
		this.descripcion = descripcion;
	}

	public ProductoDTO(com.tamscrap.model.Producto producto) {
		this.id = producto.getId();
		this.nombre = producto.getNombre();
		this.precio = producto.getPrecio();
		this.imagen = producto.getImagen();
		this.lettering = producto.isLettering();
		this.scrapbooking = producto.isScrapbooking();
		this.oferta = producto.isOferta();
		this.descuento = producto.getDescuento();
		this.precioOriginal = producto.getPrecioOriginal();
		this.cantidad = producto.getCantidad();
		this.descripcion = producto.getDescripcion();
	}

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

	public Double getPrecioOriginal() {
		if (this.oferta && this.descuento != null && this.descuento > 0) {
			double calc = this.precio / (1 - this.descuento / 100.0);
			calc = Math.round(calc * 100.0) / 100.0;
			return calc;
		}
		return this.precioOriginal;
	}

	public void setPrecioOriginal(Double precioOriginal) {
		this.precioOriginal = precioOriginal;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
