package com.tamscrap.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class PedidoDTO {
    private Long id;
    private double precio;
    private LocalDateTime fechaCreacion;
    private ClienteDTO cliente; // Agregado
    private Set<ProductoPedidoDTO> productos;
    private String direccionEnvio;
    private String metodoPago;
    private String estado;

    public PedidoDTO() {
        productos = new HashSet<>();
    }

    // Getters y Setters

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

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
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
