package com.tamscrap.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tamscrap.model.Pedido;
import com.tamscrap.model.Producto;
import com.tamscrap.model.ProductosPedidos;
import com.tamscrap.repository.PedidoRepo;
import com.tamscrap.service.PedidoService;
import com.tamscrap.service.ProductoService;

import jakarta.transaction.Transactional;

@Service
public class PedidoServiceImpl implements PedidoService {

	private final PedidoRepo pedidoRepository;
	private final ProductoService productoService;

	@Autowired
	public PedidoServiceImpl(PedidoRepo pedidoRepository, ProductoService productoService) {
		this.pedidoRepository = pedidoRepository;
		this.productoService = productoService;
	}

	@Override
	public List<Pedido> obtenerTodos() {
		return pedidoRepository.findAll();
	}

	@Override
	public Pedido obtenerPorId(Long id) {
		return pedidoRepository.findById(id).orElse(null);
	}


    @Override
    @Transactional
    public Pedido insertarPedido(Pedido pedido) {
        // Verificar stock
        for (ProductosPedidos pp : pedido.getProductos()) {
            Producto producto = pp.getProducto();
            int cantidadSolicitada = pp.getCantidad();

            if (producto.getCantidad() < cantidadSolicitada) {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + producto.getNombre());
            }
        }

        // Reducir stock
        for (ProductosPedidos pp : pedido.getProductos()) {
            Producto producto = pp.getProducto();
            int cantidadSolicitada = pp.getCantidad();
            producto.setCantidad(producto.getCantidad() - cantidadSolicitada);
            productoService.insertarProducto(producto); // Actualizar producto
        }

        // Guardar el pedido
        pedido.calcularPrecio();
        return pedidoRepository.save(pedido);
    }
	@Override
	public void eliminarPedido(Long id) {
		pedidoRepository.deleteById(id);
	}

}