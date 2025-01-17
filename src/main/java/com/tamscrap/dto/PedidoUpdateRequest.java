package com.tamscrap.dto;

import com.tamscrap.model.Pedido;

import java.util.List;

public class PedidoUpdateRequest {
    private Pedido pedido;
    private List<Integer> cantidades;
 private int clienteId;
    // Getters y setters
    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public List<Integer> getCantidades() {
        return cantidades;
    }

    public int getClienteId() {
		return clienteId;
	}

	public void setClienteId(int clienteId) {
		this.clienteId = clienteId;
	}

	public void setCantidades(List<Integer> cantidades) {
        this.cantidades = cantidades;
    }
}
