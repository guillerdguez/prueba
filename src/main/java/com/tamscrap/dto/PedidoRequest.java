package com.tamscrap.dto;

import com.tamscrap.model.Pedido;

import java.util.List;

public class PedidoRequest {
    private Pedido pedido;
    private List<Long> productoIds;

    // Getters y setters
    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public List<Long> getProductoIds() {
        return productoIds;
    }

    public void setProductoIds(List<Long> productoIds) {
        this.productoIds = productoIds;
    }
}
