package com.tamscrap.controller;

import java.util.Set;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tamscrap.model.Carrito;
import com.tamscrap.model.Cliente;
import com.tamscrap.model.Producto;
import com.tamscrap.repository.ClienteRepo;
import com.tamscrap.repository.ProductoRepo;
import com.tamscrap.service.impl.ClienteServiceImpl;
import com.tamscrap.service.impl.ProductoServiceImpl;

import jakarta.servlet.http.HttpSession;
@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "http://localhost:4200/")
public class CarritoController {

    private final ProductoRepo productRepo;
    private final ClienteRepo userRepo;
    private final HttpSession session;
    private final ProductoServiceImpl productoService;
    private final ClienteServiceImpl clienteService;

    private static final Logger logger = Logger.getLogger(CarritoController.class.getName());

    public CarritoController(ProductoRepo productRepo, ClienteRepo userRepo, HttpSession session,
                             ProductoServiceImpl productoService, ClienteServiceImpl clienteService) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.session = session;
        this.productoService = productoService;
        this.clienteService = clienteService;
    }

    // GET OR CREATE CARRO DE CLIENTE
    private Carrito obtenerOCrearCarrito(Cliente cliente) {
        if (cliente.getCarrito() == null) {
            Carrito nuevoCarrito = new Carrito(cliente.getNombre());
            cliente.setCarrito(nuevoCarrito);
            clienteService.insertarCliente(cliente);  
        }
        return cliente.getCarrito();
    }

    // READ PRODUCTS IN CART
    @GetMapping("/productos")
    public ResponseEntity<Set<Producto>> mostrarProductosCarrito() {
        logger.info("Obteniendo productos en el carrito");
        
     
        Long clienteId = (Long) session.getAttribute("cliente_id");
        if (clienteId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Cliente cliente = clienteService.obtenerPorId(clienteId);
        if (cliente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Carrito carrito = obtenerOCrearCarrito(cliente);
        return new ResponseEntity<>(carrito.getProductos(), HttpStatus.OK);
    }

    // ADD PRODUCT TO CART
    @PostMapping("/addProducto/{id}")
    public ResponseEntity<Void> agregarProductoAlCarrito(@PathVariable Long id) {
        logger.info("Agregando producto al carrito con ID: " + id);

        Long clienteId = (Long) session.getAttribute("cliente_id");
        if (clienteId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Cliente cliente = clienteService.obtenerPorId(clienteId);
        if (cliente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Carrito carrito = obtenerOCrearCarrito(cliente);
        Producto producto = productoService.obtenerPorId(id);
        if (producto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        carrito.addProducto(producto);
        clienteService.insertarCliente(cliente);  
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // REMOVE PRODUCT FROM CART
    @DeleteMapping("/removeProducto/{id}")
    public ResponseEntity<Void> eliminarProductoDelCarrito(@PathVariable Long id) {
        logger.info("Eliminando producto del carrito con ID: " + id);

        Long clienteId = (Long) session.getAttribute("cliente_id");
        if (clienteId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Cliente cliente = clienteService.obtenerPorId(clienteId);
        if (cliente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Carrito carrito = obtenerOCrearCarrito(cliente);
        Producto producto = productoService.obtenerPorId(id);
        if (producto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        carrito.removeProducto(producto);
        clienteService.insertarCliente(cliente); 
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // CALCULATE TOTAL PRICE OF CART
    @GetMapping("/checkout/total")
    public ResponseEntity<Double> calcularTotalCarrito() {
        logger.info("Calculando el total del carrito");

        Long clienteId = (Long) session.getAttribute("cliente_id");
        if (clienteId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Cliente cliente = clienteService.obtenerPorId(clienteId);
        if (cliente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Carrito carrito = obtenerOCrearCarrito(cliente);
        double total = carrito.getProductos().stream().mapToDouble(Producto::getPrecio).sum();
        return new ResponseEntity<>(total, HttpStatus.OK);
    }
}

