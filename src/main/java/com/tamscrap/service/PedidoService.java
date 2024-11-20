package com.tamscrap.service;

import com.tamscrap.model.Pedido;

import java.util.List;

public interface PedidoService {

	Pedido obtenerPorId(Long id);

	Pedido insertarPedido(Pedido pedido);

	void eliminarPedido(Long id);

	List<Pedido> obtenerTodos();

}
