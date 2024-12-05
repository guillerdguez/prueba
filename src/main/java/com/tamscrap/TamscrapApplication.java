package com.tamscrap;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;	
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tamscrap.model.Cliente;
import com.tamscrap.model.Producto;
import com.tamscrap.model.UserAuthority;
import com.tamscrap.repository.ClienteRepo;
import com.tamscrap.repository.PedidoRepo;
import com.tamscrap.repository.ProductoRepo;

@SpringBootApplication
public class TamscrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(TamscrapApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(ClienteRepo clienteRepo, ProductoRepo productoRepo, PedidoRepo pedidoRepo, PasswordEncoder encoder) {
        return args -> {
            // Creación de clientes
        	Cliente user1 = new Cliente(
        		    "User One",
        		    "user1",
        		    encoder.encode("1234"),
        		    "user1@localhost",
        		    new HashSet<>(Arrays.asList(UserAuthority.ADMIN, UserAuthority.USER))
        		);

        		Cliente user2 = new Cliente(
        		    "User Two",
        		    "user2",
        		    encoder.encode("1234"),
        		    "user2@localhost",
        		    new HashSet<>(Collections.singletonList(UserAuthority.USER))
        		);

        		clienteRepo.save(user1);
        		clienteRepo.save(user2);


            // Productos de Lettering con más variedad
            List<Producto> productosLettering = List.of(
                    crearProductoConPrecioOriginal("Brush Pen", 3.99, "https://www.milbby.com/cdn/shop/products/33614921212_2_20_1000x1000.jpg?v=1709233766", true, false, true, 10, 50),
                    crearProductoConPrecioOriginal("Rotuladores de punta fina", 5.99, "https://master.opitec.com/out/pictures/master/product/1/623095-000-000-VO-01-z.jpg", true, false, false, null, 30),
                    crearProductoConPrecioOriginal("Guía de caligrafía avanzada", 12.99, "https://dummyimage.com/200x200/fff/aaa", true, false, true, 58, 0),
                    crearProductoConPrecioOriginal("Tinta premium para Lettering", 8.49, "https://dummyimage.com/200x200/fff/aaa", true, false, true, 30, 10),
                    crearProductoConPrecioOriginal("Set de tarjetas creativas", 7.00, "https://dummyimage.com/200x200/fff/aaa", true, false, false, null, 35)
            );

            // Productos de Scrapbooking con más variedad
            List<Producto> productosScrapbooking = List.of(
                    crearProductoConPrecioOriginal("Kit de papeles decorativos", 12.99, "https://dummyimage.com/200x200/fff/aaa", false, true, true, 10, 15),
                    crearProductoConPrecioOriginal("Tijeras decorativas de borde", 6.99, "https://dummyimage.com/200x200/fff/aaa", false, true, false, null, 25),
                    crearProductoConPrecioOriginal("Pegamento resistente para Scrapbooking", 5.50, "https://dummyimage.com/200x200/fff/aaa", false, true, false, null, 0),
                    crearProductoConPrecioOriginal("Cinta Washi con patrones", 3.99, "https://dummyimage.com/200x200/fff/aaa", false, true, true, 70, 40),
                    crearProductoConPrecioOriginal("Plantillas de formas geométricas", 8.49, "https://dummyimage.com/200x200/fff/aaa", false, true, true, 10, 20)
            );

            // Productos de Papelería General con más variedad
            List<Producto> productosPapeleria = List.of(
                    crearProductoConPrecioOriginal("Cuaderno rayado A4", 4.99, "https://dummyimage.com/200x200/fff/aaa", false, false, true, 5, 100),
                    crearProductoConPrecioOriginal("Bolígrafos metálicos", 7.99, "https://dummyimage.com/200x200/fff/aaa", false, false, false, null, 80),
                    crearProductoConPrecioOriginal("Sacapuntas eléctrico", 12.49, "https://dummyimage.com/200x200/fff/aaa", false, false, true, 10, 0),
                    crearProductoConPrecioOriginal("Notas adhesivas de colores", 6.99, "https://dummyimage.com/200x200/fff/aaa", false, false, false, null, 70),
                    crearProductoConPrecioOriginal("Regla flexible 30 cm", 2.99, "https://dummyimage.com/200x200/fff/aaa", false, false, false, null, 90)
            );

            // Productos favoritos adicionales
            List<Producto> productosFavoritos = List.of(
                    crearProductoFavorito("Set de acuarelas", 14.99, "https://dummyimage.com/200x200/fff/aaa", true, false, true, 60, 15),
                    crearProductoFavorito("Cinta adhesiva doble cara", 3.49, "https://dummyimage.com/200x200/fff/aaa", false, true, false, null, 60),
                    crearProductoFavorito("Rotuladores fluorescentes", 9.50, "https://dummyimage.com/200x200/fff/aaa", true, false, false, null, 0),
                    crearProductoFavorito("Organizador para escritorio", 11.49, "https://dummyimage.com/200x200/fff/aaa", false, false, true, 5, 20),
                    crearProductoFavorito("Caja de clips metálicos", 4.99, "https://dummyimage.com/200x200/fff/aaa", false, false, false, null, 100)
            );

            // Guardar todos los productos en el repositorio
            productoRepo.saveAll(productosLettering);
            productoRepo.saveAll(productosScrapbooking);
            productoRepo.saveAll(productosPapeleria);
            productoRepo.saveAll(productosFavoritos);
        };
    }


    // Método auxiliar para crear productos calculando precioOriginal
    private Producto crearProductoConPrecioOriginal(String nombre, double precio, String imagen,
                                                    boolean lettering, boolean scrapbooking, boolean oferta, Integer descuento, int cantidad) {
        Double precioOriginal = (descuento != null && descuento > 0) 
            ? precio / (1 - (descuento / 100.0)) 
            : precio;

        return new Producto(null, nombre, precio, imagen, lettering, scrapbooking, oferta, descuento, cantidad, false, precioOriginal);
    }

    // Método auxiliar para crear productos marcados como favoritos
    private Producto crearProductoFavorito(String nombre, double precio, String imagen,
                                           boolean lettering, boolean scrapbooking, boolean oferta, Integer descuento, int cantidad) {
        Double precioOriginal = (descuento != null && descuento > 0) 
            ? precio / (1 - (descuento / 100.0)) 
            : precio;

        return new Producto(null, nombre, precio, imagen, lettering, scrapbooking, oferta, descuento, cantidad, true, precioOriginal);
    }
}
