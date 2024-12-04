package com.tamscrap.service;

import com.tamscrap.model.Cliente;

import java.util.List;

public interface ClienteService {

    Cliente obtenerPorId(Long id);

    Cliente obtenerPorUsername(String username);

    Cliente insertarCliente(Cliente cliente);

    void eliminarCliente(Long id);

    List<Cliente> obtenerTodos();
}
