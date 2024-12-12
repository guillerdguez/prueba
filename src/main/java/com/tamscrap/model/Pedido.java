package com.tamscrap.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "PEDIDOS")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Este campo representa el total del pedido, ya existente
    @Column(name = "precio")
    private double precio;

    @Column(name = "fecha")
    private LocalDateTime fechaCreacion;

    @Column(name = "estado")
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = true)
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProductosPedidos> productos;
  
     @Column(name = "direccion_envio")
    private String direccionEnvio;
//mirar como seria una forma profesinal de pagar
    @Column(name = "metodo_pago")
    private String metodoPago;

    public Pedido() {
        productos = new HashSet<>();
    }

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        productos = new HashSet<>();
    }

    public Pedido(Long id, double precio, LocalDateTime fechaCreacion, String estado, Cliente cliente, 
                  Set<ProductosPedidos> productos, String direccionEnvio, String metodoPago) {
        this.id = id;
        this.precio = precio;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.cliente = cliente;
        this.productos = productos;
        this.direccionEnvio = direccionEnvio;
        this.metodoPago = metodoPago;
    }

    public Pedido(LocalDateTime fechaCreacion, Cliente cliente) {
        this.fechaCreacion = fechaCreacion;
        this.cliente = cliente;
        this.productos = new HashSet<>();
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Set<ProductosPedidos> getProductos() {
        return productos;
    }

    public void setProductos(Set<ProductosPedidos> productos) {
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

    public void addProducto(Producto producto, int cantidad) {
        ProductosPedidos productoPedido = new ProductosPedidos(producto, this, cantidad);
        if (productos.contains(productoPedido)) {
            productos.remove(productoPedido);
        }
        if (cantidad != 0) {
            productos.add(productoPedido);
        }
        producto.getPedidos().add(productoPedido);
    }

    public void addProducto2(Producto producto, int cantidad) {
        ProductosPedidos productoPedido = new ProductosPedidos(producto, this, cantidad);
        if (productos.contains(productoPedido)) {
            productos.remove(productoPedido);
        }
        if (cantidad != 0) {
            productos.add(productoPedido);
        }
    }

    public void removeProducto(Producto producto) {
        Iterator<ProductosPedidos> iterator = productos.iterator();
        while (iterator.hasNext()) {
            ProductosPedidos productoPedido = iterator.next();
            if (productoPedido.getPedido().equals(this) && productoPedido.getProducto().equals(producto)) {
                iterator.remove();
                productoPedido.getProducto().getPedidos().remove(productoPedido);
                productoPedido.setPedido(null);
                productoPedido.setProducto(null);
                productoPedido.setCantidad(0);
            }
        }
    }

    public void calcularPrecio() {
        precio = productos.stream()
                          .mapToDouble(p -> p.getProducto().getPrecio() * p.getCantidad())
                          .sum();
    }

    public String imprimirProductos() {
        StringBuilder resultado = new StringBuilder("Productos del pedido " + id + "\n");
        if (productos.isEmpty()) {
            resultado.append("No hay productos en este pedido.");
        } else {
            for (ProductosPedidos productoPedido : productos) {
                Producto producto = productoPedido.getProducto();
                int cantidad = productoPedido.getCantidad();
                resultado.append(producto.getNombre())
                         .append(" ---> Cantidad: ").append(cantidad)
                         .append(" | Precio Unitario: ").append(producto.getPrecio()).append(" € | Total: ")
                         .append(producto.getPrecio() * cantidad).append(" €\n");
            }
            resultado.append("\n\n");
        }
        return resultado.toString();
    }

//    @Override
//	public int hashCode() {
//		return Objects.hash(cliente, direccionEnvio, estado, fechaCreacion, id, metodoPago, precio);
//	}
//
//    @Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Pedido other = (Pedido) obj;
//		return Objects.equals(cliente, other.cliente) && Objects.equals(direccionEnvio, other.direccionEnvio)
//				&& Objects.equals(estado, other.estado) && Objects.equals(fechaCreacion, other.fechaCreacion)
//				&& Objects.equals(id, other.id) && Objects.equals(metodoPago, other.metodoPago)
//				&& Double.doubleToLongBits(precio) == Double.doubleToLongBits(other.precio);
//	}

 
    
}
