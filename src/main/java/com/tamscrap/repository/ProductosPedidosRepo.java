package com.tamscrap.repository;


import com.tamscrap.model.ProductoPedidoId;
import com.tamscrap.model.ProductosPedidos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductosPedidosRepo extends JpaRepository<ProductosPedidos, ProductoPedidoId> {

}