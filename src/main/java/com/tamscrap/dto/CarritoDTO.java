package com.tamscrap.dto;

import java.util.Set;

public class CarritoDTO {
    private Long id;
    private String nombreCliente;
     private Set<CarritoProductoDTO> productos;

    // Getters y Setters

    public CarritoDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public CarritoDTO(Long id, String nombreCliente,   Set<CarritoProductoDTO> productos) {
        this.id = id;
        this.nombreCliente = nombreCliente;
         this.productos = productos;
    }
    public String getNombreCliente() {
        return nombreCliente;
    }
    
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
//
//    public String getImagenUrl() {
//        return imagenUrl;
//    }
//    
//    public void setImagenUrl(String imagenUrl) {
//        this.imagenUrl = imagenUrl;
//    }

    public Set<CarritoProductoDTO> getProductos() {
        return productos;
    }
    
    public void setProductos(Set<CarritoProductoDTO> productos) {
        this.productos = productos;
    }
 
}
