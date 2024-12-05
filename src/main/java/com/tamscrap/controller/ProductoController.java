package com.tamscrap.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tamscrap.dto.ProductoDTO;
import com.tamscrap.model.Producto;
import com.tamscrap.service.ClienteService;
import com.tamscrap.service.impl.ProductoServiceImpl;

@RestController
@RequestMapping("/api/producto")
@CrossOrigin(origins = "http://localhost:4200/")
public class ProductoController {

    private final ProductoServiceImpl productoService;
    private final ClienteService clienteService;
    private static final Logger logger = Logger.getLogger(ProductoController.class.getName());

    public ProductoController(ProductoServiceImpl productoService, ClienteService clienteService) {
        this.productoService = productoService;
        this.clienteService = clienteService;
    }

    // CREATE
    @PostMapping("/addProducto")
    public ResponseEntity<ProductoDTO> guardarProducto(@RequestBody ProductoDTO productoDTO) {
        logger.log(Level.INFO, "Producto recibido: {0}", productoDTO);
        Producto producto = convertirADto(productoDTO);
        Producto savedProducto = productoService.insertarProducto(producto);
        return new ResponseEntity<>(convertirAProductoDTO(savedProducto), HttpStatus.CREATED);
    }

    // READ
    @GetMapping("/listar")
    public ResponseEntity<List<ProductoDTO>> obtenerTodosLosProductos() {
        List<ProductoDTO> productos = productoService.obtenerTodos()
                .stream()
                .map(this::convertirAProductoDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @GetMapping("/ver/{id}")
    public ResponseEntity<ProductoDTO> obtenerProductoPorId(@PathVariable Long id) {
        logger.log(Level.INFO, "Obteniendo producto con ID: {0}", id);
        Producto producto = productoService.obtenerPorId(id);
        if (producto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(convertirAProductoDTO(producto), HttpStatus.OK);
    }

    @GetMapping("/lettering")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosLettering() {
        List<ProductoDTO> productos = productoService.ObtenerProductosLettering().stream()
                .map(this::convertirAProductoDTO).collect(Collectors.toList());
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @GetMapping("/scrapbooking")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosScrapbooking() {
        List<ProductoDTO> productos = productoService.ObtenerProductosScrapbooking().stream()
                .map(this::convertirAProductoDTO).collect(Collectors.toList());
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @GetMapping("/ofertas")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosOferta() {
        List<ProductoDTO> productos = productoService.ObtenerProductosOferta().stream()
                .map(this::convertirAProductoDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    // UPDATE
    @PutMapping("/editar/{id}")
    public ResponseEntity<ProductoDTO> editarProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        Producto productoExistente = productoService.obtenerPorId(id);
        if (productoExistente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        productoExistente.setNombre(productoDTO.getNombre());
        productoExistente.setPrecio(productoDTO.getPrecio());
        productoExistente.setImagen(productoDTO.getImagen());
        productoExistente.setLettering(productoDTO.isLettering());
        productoExistente.setScrapbooking(productoDTO.isScrapbooking());
        productoExistente.setOferta(productoDTO.isOferta());
        productoExistente.setDescuento(productoDTO.getDescuento());
        productoExistente.setPrecioOriginal(productoDTO.getPrecioOriginal());
        productoExistente.setCantidad(productoDTO.getCantidad());

        Producto updatedProducto = productoService.insertarProducto(productoExistente);
        return new ResponseEntity<>(convertirAProductoDTO(updatedProducto), HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        logger.log(Level.INFO, "Producto con ID {0} eliminado", id);
        return new ResponseEntity<>("Producto eliminado con éxito", HttpStatus.NO_CONTENT);
    }

    // FAVORITOS
    @PostMapping("/{clienteId}/favorito/{productoId}")
    public ResponseEntity<String> agregarAFavoritos(@PathVariable Long clienteId, @PathVariable Long productoId) {
        clienteService.agregarAFavoritos(clienteId, productoId);
        return new ResponseEntity<>("Producto agregado a favoritos", HttpStatus.OK);
    }

    @DeleteMapping("/{clienteId}/favorito/{productoId}")
    public ResponseEntity<String> eliminarDeFavoritos(@PathVariable Long clienteId, @PathVariable Long productoId) {
        clienteService.eliminarDeFavoritos(clienteId, productoId);
        return new ResponseEntity<>("Producto eliminado de favoritos", HttpStatus.OK);
    }

    @GetMapping("/{clienteId}/favoritos")
    public ResponseEntity<List<ProductoDTO>> obtenerFavoritos(@PathVariable Long clienteId) {
        List<ProductoDTO> favoritos = clienteService.obtenerFavoritos(clienteId).stream()
                .map(this::convertirAProductoDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(favoritos, HttpStatus.OK);
    }

    // Métodos de conversión
    private ProductoDTO convertirAProductoDTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setPrecio(producto.getPrecio());
        dto.setImagen(producto.getImagen());
        dto.setLettering(producto.isLettering());
        dto.setScrapbooking(producto.isScrapbooking());
        dto.setOferta(producto.isOferta());
        dto.setDescuento(producto.getDescuento());
        dto.setPrecioOriginal(producto.getPrecioOriginal());
        dto.setCantidad(producto.getCantidad());
        return dto;
    }

    private Producto convertirADto(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setImagen(dto.getImagen());
        producto.setLettering(dto.isLettering());
        producto.setScrapbooking(dto.isScrapbooking());
        producto.setOferta(dto.isOferta());
        producto.setDescuento(dto.getDescuento());
        producto.setPrecioOriginal(dto.getPrecioOriginal());
        producto.setCantidad(dto.getCantidad());
        return producto;
    }
}
