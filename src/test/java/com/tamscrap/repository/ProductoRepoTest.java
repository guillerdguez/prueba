//package com.tamscrap.repository;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//import java.util.Optional;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.dao.DataIntegrityViolationException;
//
//import com.tamscrap.TestConfig;
//import com.tamscrap.model.Producto;
//
//@DataJpaTest
//@Import(TestConfig.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class ProductoRepoTest {
//
//	@Autowired
//	private ProductoRepo productoRepositorio;
//
//	@Test
//	public void pruebaCuandoSeGuardaEntoncesElementoEsEncontrado() {
//		final Producto producto1 = new Producto("nombre", 1.0, "imagen");
//
//		productoRepositorio.save(producto1);
//		final Producto producto2 = productoRepositorio.getReferenceById(producto1.getId());
//
//		assertThat(producto2).usingRecursiveComparison().isEqualTo(producto1);
//	}
//
//	@Test
//	public void pruebaCuandoSeGuardaEntoncesElementoEsEncontradoPorNombre() {
//		final Producto producto1 = new Producto("nombre", 1.0, "imagen");
//
//		productoRepositorio.save(producto1);
//		final Producto producto2 = productoRepositorio.findByNombre(producto1.getNombre());
//		assertThat(producto2).isNotNull();
//
//		assertThat(producto2).usingRecursiveComparison().isEqualTo(producto1);
//	}
//
//	@Test
//	public void pruebaCuandoSeEliminaElementoGuardadoEntoncesNoEsEncontrado() {
//		final Producto producto1 = new Producto("nombre", 1.0, "imagen");
//
//		productoRepositorio.save(producto1);
//		productoRepositorio.delete(producto1);
//		final Optional<Producto> producto2 = productoRepositorio.findById(producto1.getId());
//
//		assertThat(producto2).isEmpty();
//	}
//
//	@Test
//	public void pruebaCuandoSeGuardaEntoncesFallaSinombreYaExiste() {
//		final Producto producto1 = new Producto("nombre", 1.0, "imagen");
//		productoRepositorio.save(producto1);
//
//		final Producto producto2 = new Producto("nombre", 1.0, "imagen");
//		assertThrows(DataIntegrityViolationException.class, () -> productoRepositorio.save(producto2));
//	}
//
//}
