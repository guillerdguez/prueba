//package com.tamscrap.repository;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import com.tamscrap.TestConfig;
//import com.tamscrap.model.Cliente;
//import com.tamscrap.model.UserAuthority;
//
//@DataJpaTest
//@Import(TestConfig.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class ClienteRepoTest {
//
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//
//	@Autowired
//	private ClienteRepo clienteRepositorio;
//
//	@Test
//	public void pruebaCuandoSeGuardaEntoncesElementoEsEncontrado() {
//		final Cliente cliente1 = new Cliente("username1", "contrasena", "correo1", List.of());
//
//		clienteRepositorio.save(cliente1);
//		final Cliente cliente2 = clienteRepositorio.getReferenceById(cliente1.getId());
//
//		assertThat(cliente2).usingRecursiveComparison().isEqualTo(cliente1);
//	}
//
//	@Test
//	public void pruebaCuandoSeGuardaEntoncesElementoEsEncontradoPorusername() {
//		final Cliente cliente1 = new Cliente("username1", "contrasena", "correo1", List.of());
//
//		clienteRepositorio.save(cliente1);
//		final Optional<Cliente> cliente2 = clienteRepositorio.findByUsername(cliente1.getUsername());
//
//		assertThat(cliente2).isNotEmpty();
//		assertThat(cliente2.get()).usingRecursiveComparison().isEqualTo(cliente1);
//	}
//
//	@Test
//	public void pruebaCuandoSeEliminaElementoGuardadoEntoncesNoEsEncontrado() {
//		final Cliente cliente1 = new Cliente("username1", "contrasena", "correo1", List.of());
//
//		clienteRepositorio.save(cliente1);
//		clienteRepositorio.delete(cliente1);
//		final Optional<Cliente> cliente2 = clienteRepositorio.findById(cliente1.getId());
//
//		assertThat(cliente2).isEmpty();
//	}
//
//	@Test
//	public void pruebaCuandoSeGuardaEntoncesFallaSiusernameYaExiste() {
//		final Cliente cliente1 = new Cliente("username1", "contrasena", "correo1", List.of());
//
//		clienteRepositorio.save(cliente1);
//
//		final Cliente cliente2 = new Cliente("username1", "contrasena", "correo2", List.of());
//
//		assertThrows(DataIntegrityViolationException.class, () -> clienteRepositorio.save(cliente2));
//	}
//
//	@Test
//	public void pruebaCuandoSeGuardaEntoncesFallaSiCorreoYaExiste() {
//		final Cliente cliente1 = new Cliente("username1", "contrasena", "correo1", List.of());
//
//		clienteRepositorio.save(cliente1);
//
//		final Cliente cliente2 = new Cliente("username2", "contrasena", "correo1", List.of());
//
//		assertThrows(DataIntegrityViolationException.class, () -> clienteRepositorio.save(cliente2));
//	}
//
//	@Test
//	public void pruebaCuandoSeGuardaConAutoridadesEntoncesSeRecuperanCorrectamente() {
//		final Cliente cliente1 = new Cliente("username1", "contrasena", "correo1",
//				List.of(UserAuthority.ADMIN, UserAuthority.USER));
//
//		clienteRepositorio.save(cliente1);
//		final Cliente cliente2 = clienteRepositorio.getReferenceById(cliente1.getId());
//
//		assertThat(cliente2.getAuthorities()).hasSize(2);
//		assertThat(cliente2.getAuthorities()).extracting("authority").containsExactlyInAnyOrder("ADMIN", "USER");
//	}
//
//}
