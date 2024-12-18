package com.tamscrap.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CARRITOS")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreCliente;

    @Column
    private String imagenUrl;

    @OneToOne(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cliente cliente;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
     private Set<CarritoProducto> productos = new HashSet<>();

    public Carrito() {
    }

    public Carrito(String nombreCliente, String imagenUrl) {
        this.nombreCliente = nombreCliente;
        this.imagenUrl = imagenUrl;
    }

    public Carrito(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Carrito(Long id, String nombreCliente, String imagenUrl,   Set<CarritoProducto> productos) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.imagenUrl = imagenUrl;
 
        this.productos = productos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Set<CarritoProducto> getProductos() {
        return productos;
    }

    public void setProductos(Set<CarritoProducto> productos) {
        this.productos = productos;
    }

    public void addProducto(Producto producto, int cantidad) {
     
        CarritoProducto existente = productos.stream()
            .filter(cp -> cp.getProducto().equals(producto))
            .findFirst()
            .orElse(null);

        if (existente != null) {
            int nuevaCantidad = existente.getCantidad() + cantidad;
            // Validar stock total antes de actualizar
            if (nuevaCantidad > producto.getCantidad()) {
                throw new IllegalArgumentException("La cantidad total en el carrito excede el stock disponible.");
            }
            existente.setCantidad(nuevaCantidad);
        } else {
            // Validar stock antes de crear uno nuevo
            if (cantidad > producto.getCantidad()) {
                throw new IllegalArgumentException("La cantidad solicitada excede el stock disponible.");
            }

            CarritoProducto carritoProducto = new CarritoProducto();
            carritoProducto.setProducto(producto);
            carritoProducto.setCarrito(this);
            carritoProducto.setCantidad(cantidad);
            productos.add(carritoProducto);
        }
    }


    public void removeProducto(Producto producto) {
        productos.removeIf(carritoProducto -> carritoProducto.getProducto().equals(producto));
    }
}
