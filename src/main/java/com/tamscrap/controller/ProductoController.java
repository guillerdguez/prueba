package com.tamscrap.controller;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tamscrap.dto.ProductoDTO;
import com.tamscrap.model.Producto;
import com.tamscrap.model.ProductosPedidos;
import com.tamscrap.service.impl.ProductoServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/producto")
@CrossOrigin(origins = "http://localhost:4200/")
@Api(tags = "Controlador de Productos", description = "Endpoints para la gestión de productos")
public class ProductoController {

    private final ProductoServiceImpl productoService;
    private static final Logger logger = Logger.getLogger(ProductoController.class.getName());

    public ProductoController(ProductoServiceImpl productoService) {
        this.productoService = productoService;
    }

    // CREATE
    @ApiOperation(value = "Agregar un nuevo producto", notes = "Crea un nuevo producto en la base de datos")
    @PostMapping("/addProducto")
    public ResponseEntity<ProductoDTO> guardarProducto(
            @ApiParam(value = "DTO del producto a agregar", required = true) @RequestBody ProductoDTO productoDTO) {
        logger.log(Level.INFO, "Producto recibido: {0}", productoDTO);
        Producto producto = convertirADto(productoDTO);
        Producto savedProducto = productoService.insertarProducto(producto);
        return new ResponseEntity<>(convertirAProductoDTO(savedProducto), HttpStatus.CREATED);
    }

    // READ
    @ApiOperation(value = "Listar todos los productos", notes = "Obtiene una lista de todos los productos disponibles")
    @GetMapping("/listar")
    public ResponseEntity<List<ProductoDTO>> obtenerTodosLosProductos() {
        List<ProductoDTO> productos = productoService.obtenerTodos()
                .stream()
                .map(this::convertirAProductoDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @ApiOperation(value = "Obtener un producto por ID", notes = "Devuelve el producto correspondiente al ID especificado")
    @GetMapping("/ver/{id}")
    public ResponseEntity<ProductoDTO> obtenerProductoPorId(
            @ApiParam(value = "ID del producto a obtener", required = true) @PathVariable Long id) {
        logger.log(Level.INFO, "Obteniendo producto con ID: {0}", id);
        Producto producto = productoService.obtenerPorId(id);
        if (producto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(convertirAProductoDTO(producto), HttpStatus.OK);
    }

    @ApiOperation(value = "Obtener productos de lettering", notes = "Devuelve una lista de productos categorizados como lettering")
    @GetMapping("/lettering")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosLettering() {
        List<ProductoDTO> productos = productoService.ObtenerProductosLettering().stream()
                .map(this::convertirAProductoDTO).collect(Collectors.toList());
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @ApiOperation(value = "Obtener productos de scrapbooking", notes = "Devuelve una lista de productos categorizados como scrapbooking")
    @GetMapping("/scrapbooking")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosScrapbooking() {
        List<ProductoDTO> productos = productoService.ObtenerProductosScrapbooking().stream()
                .map(this::convertirAProductoDTO).collect(Collectors.toList());
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @ApiOperation(value = "Obtener productos en oferta", notes = "Devuelve una lista de productos en oferta")
    @GetMapping("/ofertas")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosOferta() {
        List<ProductoDTO> productos = productoService.ObtenerProductosOferta().stream()
                .map(this::convertirAProductoDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    // UPDATE
    @ApiOperation(value = "Editar un producto existente", notes = "Actualiza la información del producto especificado por el ID")
    @PutMapping("/editar/{id}")
    public ResponseEntity<ProductoDTO> editarProducto(
            @ApiParam(value = "ID del producto a editar", required = true) @PathVariable Long id,
            @ApiParam(value = "DTO del producto con la nueva información", required = true) @RequestBody ProductoDTO productoDTO) {
        Producto productoExistente = productoService.obtenerPorId(id);
        if (productoExistente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Actualizar detalles del producto existente
        productoExistente.setNombre(productoDTO.getNombre());
        productoExistente.setPrecio(productoDTO.getPrecio());
        productoExistente.setImagen(productoDTO.getImagen());
        productoExistente.setLettering(productoDTO.isLettering());
        productoExistente.setScrapbooking(productoDTO.isScrapbooking());
        productoExistente.setOferta(productoDTO.isOferta());
        productoExistente.setDescuento(productoDTO.getDescuento());
        productoExistente.setFavorito(productoDTO.isFavorito());
        productoExistente.setPrecioOriginal(productoDTO.getPrecioOriginal());

        // Solo actualizar pedidos si no son nulos
        if (productoDTO.getPedidos() != null) {
            Set<ProductosPedidos> nuevosPedidos = productoDTO.getPedidos().stream()
                    .map(dto -> dto.toProductosPedidos(id))
                    .collect(Collectors.toSet());

            productoExistente.getPedidos().clear();
            productoExistente.getPedidos().addAll(nuevosPedidos);
        }

        Producto updatedProducto = productoService.insertarProducto(productoExistente);
        return new ResponseEntity<>(convertirAProductoDTO(updatedProducto), HttpStatus.OK);
    }

    // DELETE
    @ApiOperation(value = "Eliminar un producto", notes = "Elimina el producto especificado por el ID")
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> eliminarProducto(
            @ApiParam(value = "ID del producto a eliminar", required = true) @PathVariable Long id) {
        productoService.eliminarProducto(id);
        logger.log(Level.INFO, "Producto con ID {0} eliminado", id);
        return new ResponseEntity<>("Producto eliminado con éxito", HttpStatus.NO_CONTENT);
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
        dto.setFavorito(producto.isFavorito());
        dto.setPrecioOriginal(producto.getPrecioOriginal());
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
        producto.setFavorito(dto.isFavorito());
        producto.setPrecioOriginal(dto.getPrecioOriginal());
        return producto;
    }
}
