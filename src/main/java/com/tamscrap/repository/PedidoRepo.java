package com.tamscrap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tamscrap.model.Pedido;

@Repository
public interface PedidoRepo extends JpaRepository<Pedido, Long> {

 }
