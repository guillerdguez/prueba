package com.tamscrap.dto;

import java.util.List;

public class PedidoRequestDTO {
    private double precio;
    private String estado;
    private Long clienteId; // Asegúrate de que este campo está presente
    private String direccionEnvio;
    private String metodoPago;
    private List<Long> productoIds;
    private List<Integer> cantidades;

    // Getters y Setters

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
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

    public List<Long> getProductoIds() {
        return productoIds;
    }

    public void setProductoIds(List<Long> productoIds) {
        this.productoIds = productoIds;
    }

    public List<Integer> getCantidades() {
        return cantidades;
    }

    public void setCantidades(List<Integer> cantidades) {
        this.cantidades = cantidades;
    }

    @Override
    public String toString() {
        return "PedidoRequestDTO{" +
                "precio=" + precio +
                ", estado='" + estado + '\'' +
                ", clienteId=" + clienteId +
                ", direccionEnvio='" + direccionEnvio + '\'' +
                ", metodoPago='" + metodoPago + '\'' +
                ", productoIds=" + productoIds +
                ", cantidades=" + cantidades +
                '}';
    }
}
