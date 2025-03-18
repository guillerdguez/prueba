package com.tamscrap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tamscrap.dto.ProductoDTO;
import com.tamscrap.model.Producto;

@Repository
public interface ProductoRepo extends JpaRepository<Producto, Long> {
	Producto findByNombre(String nombre);
	@Query("SELECT new com.tamscrap.dto.ProductoDTO(p) FROM Producto p WHERE p.nombre = :nombre")
	ProductoDTO findByNombreDto(@Param("nombre") String nombre);

	@Query("SELECT new com.tamscrap.dto.ProductoDTO(p) FROM Producto p")
	List<ProductoDTO> findAllAsDTO();

	@Query("SELECT new com.tamscrap.dto.ProductoDTO(p) FROM Producto p WHERE p.lettering = :lettering")
	List<ProductoDTO> findByLettering(@Param("lettering") boolean lettering);

	@Query("SELECT new com.tamscrap.dto.ProductoDTO(p) FROM Producto p WHERE p.scrapbooking = :scrapbooking")
	List<ProductoDTO> findByScrapbooking(@Param("scrapbooking") boolean scrapbooking);

	@Query("SELECT new com.tamscrap.dto.ProductoDTO(p) FROM Producto p WHERE p.oferta = :oferta")
	List<ProductoDTO> findByOferta(@Param("oferta") boolean oferta);

	@Query("SELECT new com.tamscrap.dto.ProductoDTO(p) FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
	List<ProductoDTO> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
}
