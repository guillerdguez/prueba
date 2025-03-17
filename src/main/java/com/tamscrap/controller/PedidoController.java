package com.tamscrap.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.tamscrap.dto.ClienteDTOListarPedidos;
import com.tamscrap.dto.PedidoDTOListar;
import com.tamscrap.dto.ProductoPedidoDTO;
import com.tamscrap.model.Cliente;
import com.tamscrap.model.Pedido;
import com.tamscrap.model.Producto;
import com.tamscrap.model.ProductosPedidos;
import com.tamscrap.service.impl.ClienteServiceImpl;
import com.tamscrap.service.impl.PedidoServiceImpl;
import com.tamscrap.service.impl.ProductoServiceImpl;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = { "http://localhost:4200", "https://tamscrapt.up.railway.app" })
public class PedidoController {

    private final ProductoServiceImpl productoService;
    private final PedidoServiceImpl pedidoService;
    private final ClienteServiceImpl clienteService;
    private static final Logger logger = Logger.getLogger(PedidoController.class.getName());

    public PedidoController(ProductoServiceImpl productoService, PedidoServiceImpl pedidoService,
                            ClienteServiceImpl clienteService) {
        this.productoService = productoService;
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
    }

    // CREATE
    @PostMapping("/addPedido")
    public ResponseEntity<Void> guardarPedido(@RequestBody Pedido pedido, @AuthenticationPrincipal Cliente cliente) {

        Long clienteId = cliente.getId();

        logger.log(Level.INFO, "Pedido recibido: {0}", pedido);
        System.err.println(clienteId + " Pedidodddddd");
        if (pedido.getDireccionEnvio() == null || pedido.getDireccionEnvio().trim().isEmpty()) {
            logger.log(Level.WARNING, "La dirección de envío es requerida.");
            return ResponseEntity.badRequest().build();
        }

        if (pedido.getMetodoPago() == null || pedido.getMetodoPago().trim().isEmpty()) {
            logger.log(Level.WARNING, "El método de pago es requerido.");
            return ResponseEntity.badRequest().build();
        }

        try {
            pedido.setCliente(clienteService.obtenerPorId(clienteId));
            System.err.println(clienteService.obtenerPorId(clienteId) + " Pedidodddddd");
            pedidoService.insertarPedido(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Error al crear el pedido: {0}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error interno al crear el pedido", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // READ
    @GetMapping("/listar")
    public ResponseEntity<List<PedidoDTOListar>> mostrarPedidos() {
        logger.log(Level.INFO, "Obteniendo todos los pedidos");
        List<PedidoDTOListar> pedidos = pedidoService.obtenerTodos().stream()
                .map(this::convertirAPedidoDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @GetMapping("/pedidosCliente")
    public ResponseEntity<List<PedidoDTOListar>> mostrarPedidosPorCliente(@AuthenticationPrincipal Cliente cliente) {
        Long clienteId = cliente.getId();
        logger.log(Level.INFO, "Obteniendo los pedidos para el cliente con ID: " + clienteId);

        List<PedidoDTOListar> pedidos = pedidoService
                .obtenerPorClienteId(clienteId)
                .stream()
                .map(this::convertirAPedidoDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @GetMapping("/ver/{id}")
    public ResponseEntity<?> obtenerPedido(@PathVariable Long id) {
        logger.log(Level.INFO, "Obteniendo pedido con ID: {0}", id);
        Pedido pedido = pedidoService.obtenerPorId(id);
        PedidoDTOListar pedidoDTO = convertirAPedidoDTO(pedido);
        return new ResponseEntity<>(pedidoDTO, HttpStatus.OK);
    }

    // UPDATE COMPLETO (requiere pasar cliente, dirección, método de pago, etc.)
    @PutMapping("/editar/{id}")
    public ResponseEntity<Pedido> actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido) {

        Pedido pedidoExistente = pedidoService.obtenerPorId(id);
        if (pedidoExistente == null) {
            logger.log(Level.WARNING, "No se encontró el pedido con ID: {0}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Actualizar los campos que quieras sobrescribir
        pedidoExistente.setCliente(clienteService.obtenerPorId(pedido.getCliente().getId()));
        pedidoExistente.setDireccionEnvio(pedido.getDireccionEnvio());
        pedidoExistente.setMetodoPago(pedido.getMetodoPago());
        pedidoExistente.setNombreComprador(pedido.getNombreComprador());
        pedidoExistente.setEstado(pedido.getEstado());
        // Si es necesario actualizar los productos, se debe realizar aquí o
        // en un endpoint específico

        try {
            Pedido updatedPedido = pedidoService.insertarPedido(pedidoExistente);
            return new ResponseEntity<>(updatedPedido, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al actualizar el pedido", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // NUEVO: UPDATE PARCIAL DEL ESTADO (PATCH)
    @PatchMapping("/estado/{id}")
    public ResponseEntity<Pedido> actualizarEstado(@PathVariable Long id, @RequestBody EstadoDTO estadoDTO) {
        logger.log(Level.INFO, "Actualizando SOLO el estado del pedido con ID: {0}", id);

        Pedido pedidoExistente = pedidoService.obtenerPorId(id);
        if (pedidoExistente == null) {
            logger.log(Level.WARNING, "No se encontró el pedido con ID: {0}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Aquí NO forzamos a pasar cliente, dirección, etc. Solo cambiamos el estado.
        pedidoExistente.setEstado(estadoDTO.getEstado());
        try {
            Pedido updatedPedido = pedidoService.actualizarEstado(pedidoExistente);
            return new ResponseEntity<>(updatedPedido, HttpStatus.OK);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error interno al actualizar estado del pedido", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ADD PRODUCTO
    @PostMapping("/addProducto/{id}")
    public ResponseEntity<?> agregarProducto(
            @PathVariable Long id,
            @RequestParam("idProducto") Long idProducto,
            @RequestParam("cantidad") int cantidad
    ) {
        logger.log(Level.INFO, "Agregando producto con ID {0} al pedido con ID {1}", new Object[] { idProducto, id });
        Pedido pedidoExistente = pedidoService.obtenerPorId(id);
        if (pedidoExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Producto producto = productoService.obtenerPorId(idProducto).orElseThrow();
        if (producto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        pedidoExistente.addProducto2(producto, cantidad);
        pedidoExistente.calcularPrecio();
        try {
            Pedido updatedPedido = pedidoService.insertarPedido(pedidoExistente);
            return ResponseEntity.ok(updatedPedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/removeProducto")
    public ResponseEntity<?> removeProducto(@RequestBody Map<String, Long> request) {
        Long pedidoId = request.get("pedidoId");
        Long productoId = request.get("productoId");

        logger.log(Level.INFO, "Eliminando producto con ID {0} del pedido con ID {1}",
                new Object[] { productoId, pedidoId });

        Pedido pedido = pedidoService.obtenerPorId(pedidoId);
        if (pedido == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Producto producto = productoService.obtenerPorId(productoId).orElseThrow();
        if (producto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        pedido.removeProducto(producto);
        try {
            pedidoService.insertarPedido(pedido);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al eliminar producto del pedido", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> eliminarPedido(@PathVariable Long id) {
        logger.log(Level.INFO, "Eliminando pedido con ID: {0}", id);
        try {
            pedidoService.eliminarPedido(id);
            return new ResponseEntity<>("Pedido eliminado con éxito", HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Error al eliminar pedido: {0}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error interno al eliminar pedido", e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private PedidoDTOListar convertirAPedidoDTO(Pedido pedido) {
        PedidoDTOListar dto = new PedidoDTOListar();
        dto.setId(pedido.getId());
        dto.setPrecio(pedido.getPrecio());
        dto.setFechaCreacion(pedido.getFechaCreacion());
        dto.setDireccionEnvio(pedido.getDireccionEnvio());
        dto.setMetodoPago(pedido.getMetodoPago());
        dto.setEstado(pedido.getEstado());
        dto.setNombreComprador(pedido.getNombreComprador());

        Cliente cliente = pedido.getCliente();
        if (cliente != null) {
            ClienteDTOListarPedidos clienteDTO = convertirAClienteDTOListarPedidos(cliente);
            dto.setCliente(clienteDTO);
        }

        Set<ProductoPedidoDTO> productosDTO = pedido.getProductos()
                .stream()
                .map(this::convertirAProductoPedidoDTO)
                .collect(Collectors.toSet());
        dto.setProductos(productosDTO);

        return dto;
    }

    private ProductoPedidoDTO convertirAProductoPedidoDTO(ProductosPedidos productosPedidos) {
        ProductoPedidoDTO dto = new ProductoPedidoDTO();
        dto.setProductoId(productosPedidos.getProducto().getId());
        dto.setCantidad(productosPedidos.getCantidad());
        return dto;
    }

    private ClienteDTOListarPedidos convertirAClienteDTOListarPedidos(Cliente cliente) {
        ClienteDTOListarPedidos dto = new ClienteDTOListarPedidos();
        dto.setId(cliente.getId());
        dto.setUsername(cliente.getUsername());
        dto.setNombre(cliente.getNombre());
        dto.setEmail(cliente.getEmail());
        dto.setAuthorities(cliente.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList()));
        return dto;
    }

    // DTO interno para el PATCH de estado
    public static class EstadoDTO {
        private String estado;
        public String getEstado() {
            return estado;
        }
        public void setEstado(String estado) {
            this.estado = estado;
        }
    }
}
