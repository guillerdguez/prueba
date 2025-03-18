//package com.tamscrap.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import com.tamscrap.model.Producto;
//import com.tamscrap.repository.ProductoRepo;
//import com.tamscrap.service.impl.ProductoServiceImpl;
//
//import jakarta.persistence.EntityExistsException;
//
//@ExtendWith(SpringExtension.class)
//public class ProductoServiceImplTest {
//
//	@Mock
//	private ProductoRepo productoRepository;
//
//	@InjectMocks
//	private ProductoServiceImpl productoService;
//
//	@Test
//	void cuandoInsertarProductoEntoncesProductoInsertadoOk() {
//		final Producto producto = new Producto(null, "nombre", "", 1.0, "imagen", false, false, false, null, 0, false,
//				null);
//		when(productoRepository.save(producto)).thenReturn(producto);
//
//		Producto productoInsertado = productoService.insertarProducto(producto);
//
//		verify(productoRepository).save(producto);
//		assertNotNull(productoInsertado);
//		assertThat(productoInsertado).usingRecursiveComparison().isEqualTo(producto);
//	}
//
//	@Test
//	void cuandoEliminarProductoEntoncesProductoEliminadoOk() {
//		doNothing().when(productoRepository).deleteById(anyLong());
//
//		productoService.eliminarProducto(anyLong());
//
//		verify(productoRepository).deleteById(Mockito.any(Long.class));
//	}
//
//	@Test
//	void cuandoActualizarProductoEntoncesProductoActualizadoOk() {
//		final Producto productoOriginal = new Producto(null, "nombre", "", 1.0, "imagen", false, false, false, null, 0,
//				false, null);
//		final Producto productoActualizado = new Producto(null, "nombre actualizado", "", 1.0, "imagen", false, false,
//				false, null, 0, false, null);
//		when(productoRepository.findByNombre("nombre")).thenReturn(productoOriginal);
//		when(productoRepository.save(any(Producto.class))).thenReturn(productoActualizado);
//
//		Producto productoObtenido = productoService.obtenerPorNombre("nombre");
//		productoObtenido.setNombre("nombre actualizado");
//		Producto productoGuardado = productoService.insertarProducto(productoObtenido);
//
//		verify(productoRepository).save(productoObtenido);
//		assertNotNull(productoGuardado);
//		assertThat(productoGuardado).usingRecursiveComparison().isEqualTo(productoActualizado);
//	}
//
//	@Test
//	void obtenerTodosOk() {
//		final Producto producto1 = new Producto(null, "nombre1", "", 1.0, "imagen1", false, false, false, null, 0,
//				false, null);
//		final Producto producto2 = new Producto(null, "nombre2", "", 1.0, "imagen2", false, false, false, null, 0,
//				false, null);
//		final Producto producto3 = new Producto(null, "nombre3", "", 1.0, "imagen3", false, false, false, null, 0,
//				false, null);
//
//		when(productoRepository.findAll()).thenReturn(List.of(producto1, producto2, producto3));
//
//		List<Producto> productos = productoService.obtenerTodos();
//
//		assertEquals(3, productos.size());
//		assertTrue(productos.containsAll(List.of(producto1, producto2, producto3)));
//	}
//
//	@Test
//	void cuandoInsertarProductoConNombreNuloEntoncesDevuelveExcepcion() {
//		final Producto producto = new Producto(null, "nombre", "", 1.0, "imagen1", false, false, false, null, 0, false,
//				null);
//		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//				() -> productoService.insertarProducto(producto));
//		assertEquals("El nombre no puede ser nulo o vacío", exception.getMessage());
//
//	}
//
//	@Test
//	void cuandoInsertarProductoConNombreYaInsertadoEntoncesDevuelveExcepcion() {
//		final Producto productoActual1 = new Producto(null, "nombre", "", 1.0, "imagen1", false, false, false, null, 0,
//				false, null);
//
//		when(productoRepository.save(productoActual1)).thenReturn(productoActual1);
//
//		Producto productoExpected1 = productoService.insertarProducto(productoActual1);
//
//		verify(productoRepository).save(productoActual1);
//		assertNotNull(productoExpected1);
//		assertThat(productoExpected1).usingRecursiveAssertion().isEqualTo(productoActual1);
//		final Producto productoActual2 = new Producto(null, "nombre", "", 1.0, "imagen1", false, false, false, null, 0,
//				false, null);
//		when(productoRepository.save(productoActual2)).thenThrow(new EntityExistsException());
//
//		assertThrows(EntityExistsException.class, () -> productoService.insertarProducto(productoActual2));
//	}
//}
