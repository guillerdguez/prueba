package com.tamscrap.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tamscrap.dto.ProductoDTO;
import com.tamscrap.model.Producto;
import com.tamscrap.service.ClienteService;
import com.tamscrap.service.ProductoService;

@RestController
@RequestMapping("/api/producto")
//@CrossOrigin(origins = "http://localhost:4200/")
@CrossOrigin(origins = {"http://localhost:4200", "https://tamscrapt.up.railway.app"})
public class ProductoController {

	private final ProductoService productoService;
	private final ClienteService clienteService;
	private static final Logger logger = Logger.getLogger(ProductoController.class.getName());

	public ProductoController(ProductoService productoService, ClienteService clienteService) {
		this.productoService = productoService;
		this.clienteService = clienteService;
	}

	// CREATE
	@PostMapping("/addProducto")
	public ResponseEntity<Producto> guardarProducto(@RequestBody Producto  producto ) {
		logger.log(Level.INFO, "Producto recibido: {0}", producto );
		Producto savedProducto = productoService.insertarProducto(producto) ;
		return new ResponseEntity<>(savedProducto, HttpStatus.CREATED);
	}

	// READ
	@GetMapping("/listar")
    public ResponseEntity<List<ProductoDTO>> obtenerTodosLosProductos(
            @RequestParam(value = "categoria", required = false) String categoria) {

        List<ProductoDTO> productos = (categoria != null && !categoria.isEmpty())
                ? productoService.obtenerProductosPorCategoria(categoria)
                : productoService.obtenerTodos();

        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosPorCategoria(@PathVariable String categoria) {
        logger.log(Level.INFO, "Obteniendo productos de la categoría: {0}", categoria);
        List<ProductoDTO> productos = productoService.obtenerProductosPorCategoria(categoria);
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }


	@GetMapping("/buscar")
	public ResponseEntity<List<ProductoDTO>> buscarProductos(
			@RequestParam(name = "name", required = false) String name) {
		if (name == null || name.trim().isEmpty()) {
			return new ResponseEntity<>(List.of(), HttpStatus.OK);
		}
		List<ProductoDTO> productos = productoService.buscarProductos(name);
		return new ResponseEntity<>(productos, HttpStatus.OK);
	}

	@GetMapping("/ver/{id}")
	public ResponseEntity<ProductoDTO> obtenerProductoPorId(@PathVariable Long id) {
		logger.log(Level.INFO, "Obteniendo producto con ID: {0}", id);
		ProductoDTO  producto = productoService.obtenerDtoPorId(id)  ;
		return (producto != null) ? new ResponseEntity<>(producto, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	// UPDATE
	@PutMapping("/editar/{id}")
	public ResponseEntity<ProductoDTO> editarProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
		ProductoDTO updatedProducto = productoService.editarProducto(id, productoDTO);
		return (updatedProducto != null) ? new ResponseEntity<>(updatedProducto, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
		clienteService.agregarAFavoritos(clienteId, productoId);
		return new ResponseEntity<>("Producto eliminado de favoritos", HttpStatus.OK);
	}

	@GetMapping("/{clienteId}/favoritos")
	public ResponseEntity<List<ProductoDTO>> obtenerFavoritos(@PathVariable Long clienteId) {
		// Se obtiene la lista de favoritos como objetos ProductoDTO directamente
		List<ProductoDTO> favoritos = clienteService.obtenerFavoritos(clienteId);
		return new ResponseEntity<>(favoritos, HttpStatus.OK);
	}

}
