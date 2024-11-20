package com.tamscrap;

import java.util.ArrayList;
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
            Cliente user1 = new Cliente("User One", "user1", encoder.encode("1234"), "user1@localhost",
                    new ArrayList<>(List.of(UserAuthority.ADMIN, UserAuthority.USER)));

            Cliente user2 = new Cliente("User Two", "user2", encoder.encode("1234"), "user2@localhost",
                    new ArrayList<>(List.of(UserAuthority.USER)));

            clienteRepo.save(user1);
            clienteRepo.save(user2);

            // Productos de Lettering con descuentos
            List<Producto> productosLettering = List.of(
                    crearProductoConPrecioOriginal("Brush Pen", 3.99, "https://www.milbby.com/cdn/shop/products/33614921212_2_20_1000x1000.jpg?v=1709233766", true, false, true, 10),
                    crearProductoConPrecioOriginal("Rotuladores de punta fina", 5.99, "https://master.opitec.com/out/pictures/master/product/1/623095-000-000-VO-01-z.jpg", true, false, false, null),
                    crearProductoConPrecioOriginal("Guía de caligrafía", 8.99, "https://dummyimage.com/200x200/fff/aaa", true, false, true, 15),
                    crearProductoConPrecioOriginal("Tinta para Lettering", 4.50, "https://dummyimage.com/200x200/fff/aaa", true, false, false, null),
                    crearProductoConPrecioOriginal("Papel para Lettering", 6.00, "https://dummyimage.com/200x200/fff/aaa", true, false, true, 5)
            );

            // Productos de Scrapbooking con descuentos
            List<Producto> productosScrapbooking = List.of(
                    crearProductoConPrecioOriginal("Kit de papeles decorativos", 12.99, "https://dummyimage.com/200x200/fff/aaa", false, true, true, 20),
                    crearProductoConPrecioOriginal("Tijeras con formas", 4.99, "https://dummyimage.com/200x200/fff/aaa", false, true, false, null),
                    crearProductoConPrecioOriginal("Pegamento para Scrapbooking", 3.50, "https://dummyimage.com/200x200/fff/aaa", false, true, false, null),
                    crearProductoConPrecioOriginal("Troqueladora de formas", 6.99, "https://dummyimage.com/200x200/fff/aaa", false, true, true, 10),
                    crearProductoConPrecioOriginal("Stickers decorativos", 2.99, "https://dummyimage.com/200x200/fff/aaa", false, true, false, null)
            );

            // Productos de Papelería General con descuentos
            List<Producto> productosPapeleria = List.of(
                    crearProductoConPrecioOriginal("Cuaderno A5", 2.99, "https://dummyimage.com/200x200/fff/aaa", false, false, true, 5),
                    crearProductoConPrecioOriginal("Bolígrafos de colores", 1.99, "https://dummyimage.com/200x200/fff/aaa", false, false, false, null),
                    crearProductoConPrecioOriginal("Goma de borrar", 0.99, "https://dummyimage.com/200x200/fff/aaa", false, false, false, null),
                    crearProductoConPrecioOriginal("Bloc de notas adhesivas", 3.99, "https://dummyimage.com/200x200/fff/aaa", false, false, false, null),
                    crearProductoConPrecioOriginal("Lápices de grafito", 1.50, "https://dummyimage.com/200x200/fff/aaa", false, false, true, 10)
            );

            // Productos favoritos
            List<Producto> productosFavoritos = List.of(
                crearProductoFavorito("Marcadores permanentes", 7.99, "https://dummyimage.com/200x200/fff/aaa", false, false, true, 10),
                crearProductoFavorito("Álbum Scrapbooking", 15.99, "https://dummyimage.com/200x200/fff/aaa", false, true, true, 20),
                crearProductoFavorito("Kit de pinceles para Lettering", 12.50, "https://dummyimage.com/200x200/fff/aaa", true, false, false, null),
                crearProductoFavorito("Lápices acuarelables", 9.99, "https://dummyimage.com/200x200/fff/aaa", true, false, true, 15),
                crearProductoFavorito("Rotulador doble punta", 6.99, "https://dummyimage.com/200x200/fff/aaa", true, false, false, null)
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
                                                    boolean lettering, boolean scrapbooking, boolean oferta, Integer descuento) {
        Double precioOriginal = null;
        if (descuento != null && descuento > 0) {
            precioOriginal = precio / (1 - (descuento / 100.0));
        } else {
            precioOriginal = precio; // Si no hay descuento, el precio original es el precio actual
        }
        
        return new Producto(null, nombre, precio, imagen, lettering, scrapbooking, oferta, descuento, false, precioOriginal);
    }

    // Método auxiliar para crear productos marcados como favoritos
    private Producto crearProductoFavorito(String nombre, double precio, String imagen,
                                           boolean lettering, boolean scrapbooking, boolean oferta, Integer descuento) {
        Double precioOriginal = null;

        // Si hay descuento, calcular el precio original
        if (descuento != null && descuento > 0) {
            precioOriginal = precio / (1 - (descuento / 100.0));
        } else {
            precioOriginal = precio; // Si no hay descuento, el precio original es igual al precio actual
        }

        // Crear el producto y marcarlo como favorito
        return new Producto(null, nombre, precio, imagen, lettering, scrapbooking, oferta, descuento, true, precioOriginal);
    }
}
