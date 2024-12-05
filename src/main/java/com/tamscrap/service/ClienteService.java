package com.tamscrap.service;

 import java.util.List;

import com.tamscrap.model.Cliente;
import com.tamscrap.model.Producto;

public interface ClienteService {

    Cliente insertarCliente(Cliente cliente);

    List<Cliente> obtenerTodos();

    Cliente obtenerPorId(Long id);

    void eliminarCliente(Long id);

    Cliente obtenerPorUsername(String username);
 
    void agregarAFavoritos(Long clienteId, Long productoId);

    void eliminarDeFavoritos(Long clienteId, Long productoId);

    List<Producto> obtenerFavoritos(Long clienteId);
}
