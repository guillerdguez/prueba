package com.tamscrap.model;
 
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

//    @OneToMany(mappedBy = "carrito", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Producto> productos = new HashSet<>();

    @Column
    private String imagenUrl; 
    @OneToOne(mappedBy = "carrito")
    private Cliente cliente;

    @ManyToMany
    @JoinTable(
        name = "carrito_productos",
        joinColumns = @JoinColumn(name = "carrito_id"),
        inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private Set<Producto> productos = new HashSet<>();
    public Carrito() {
    }

    // Constructor con imagen opcional
    public Carrito(String nombreCliente, String imagenUrl) {
        this.nombreCliente = nombreCliente;
        this.imagenUrl = imagenUrl;
    }

    public Carrito(String nombreCliente) {
        this.nombreCliente = nombreCliente;
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

    public Set<Producto> getProductos() {
        return productos;
    }

    public void setProductos(Set<Producto> productos) {
        this.productos = productos;
    }

    public void addProducto(Producto producto) {
        productos.add(producto);
        producto.setCarrito(this);
    }

    public void removeProducto(Producto producto) {
        productos.remove(producto);
        producto.setCarrito(null);
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    @Override
    public String toString() {
        return "Carrito [id=" + id + ", nombreCliente=" + nombreCliente + ", productos=" + productos + ", imagenUrl=" + imagenUrl + "]";
    }
}
