package com.tamscrap.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProductoPedidoId implements Serializable {

    private static final long serialVersionUID = 1L;
	
    @Column(name = "PEDIDO_ID")
    private Long pedidoId;

    @Column(name = "PRODUCTO_ID")
    private Long productoId;

    public ProductoPedidoId() {}

    public ProductoPedidoId(Long pedidoId, Long productoId) {
        this.pedidoId = pedidoId;
        this.productoId = productoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductoPedidoId that = (ProductoPedidoId) o;
        return Objects.equals(pedidoId, that.pedidoId) &&
                Objects.equals(productoId, that.productoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pedidoId, productoId);
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }
}
