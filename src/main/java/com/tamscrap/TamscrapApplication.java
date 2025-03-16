package com.tamscrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TamscrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(TamscrapApplication.class, args);
    }
//
//    @Bean
//    public CommandLineRunner initData(ClienteRepo clienteRepo, ProductoRepo productoRepo, PedidoRepo pedidoRepo, PasswordEncoder encoder) {
//        return args -> {
//            // Crear y guardar clientes
//            List<Cliente> clientes = List.of(
//                new Cliente("User One", "user1", encoder.encode("1234"), "user1@localhost", Set.of(UserAuthority.ADMIN, UserAuthority.USER)),
//                new Cliente("User Two", "user2", encoder.encode("1234"), "user2@localhost", Set.of(UserAuthority.USER)),
//                new Cliente("User Three", "user3", encoder.encode("1234"), "user3@localhost", Set.of(UserAuthority.USER))
//            );
//            clienteRepo.saveAll(clientes);
//
//            // Definir y guardar productos
//            List<Producto> productos = List.of(
//                // Productos de Lettering
//                crearProducto("Brush Pen", 3.99, "https://www.milbby.com/cdn/shop/products/33614921212_2_20_1000x1000.jpg?v=1709233766", true, false, true, 10, 50, "Rotuladores con punta de pincel para caligrafía.", false),
//                crearProducto("Rotuladores de punta fina", 5.99, "https://master.opitec.com/out/pictures/master/product/1/623095-000-000-VO-01-z.jpg", true, false, false, null, 30, "Rotuladores perfectos para trazos precisos.", false),
//                crearProducto("Guía de caligrafía avanzada", 12.99, "https://dummyimage.com/200x200/fff/aaa", true, false, true, 58, 0, "Libro para mejorar tus habilidades en lettering.", false),
//                crearProducto("Tinta premium para Lettering", 8.49, "https://dummyimage.com/200x200/fff/aaa", true, false, true, 30, 10, "Tinta de alta calidad para trabajos creativos.", false),
//                crearProducto("Set de tarjetas creativas", 7.00, "https://dummyimage.com/200x200/fff/aaa", true, false, false, null, 35, "Tarjetas ideales para tus proyectos de lettering.", false),
//
//                // Productos de Scrapbooking
//                crearProducto("Kit de papeles decorativos", 12.99, "https://dummyimage.com/200x200/fff/aaa", false, true, true, 10, 15, "Papeles decorativos para álbumes y proyectos creativos.", false),
//                crearProducto("Tijeras decorativas de borde", 6.99, "https://dummyimage.com/200x200/fff/aaa", false, true, false, null, 25, "Tijeras con bordes decorativos para manualidades.", false),
//                crearProducto("Pegamento resistente para Scrapbooking", 5.50, "https://dummyimage.com/200x200/fff/aaa", false, true, false, null, 0, "Pegamento ideal para proyectos duraderos.", false),
//                crearProducto("Cinta Washi con patrones", 3.99, "https://dummyimage.com/200x200/fff/aaa", false, true, true, 70, 40, "Cinta decorativa para adornar tus creaciones.", false),
//                crearProducto("Plantillas de formas geométricas", 8.49, "https://dummyimage.com/200x200/fff/aaa", false, true, true, 10, 20, "Plantillas útiles para crear formas precisas.", false),
//
//                // Productos de Papelería General
//                crearProducto("Cuaderno rayado A4", 4.99, "https://dummyimage.com/200x200/fff/aaa", false, false, true, 5, 100, "Cuaderno con hojas rayadas de alta calidad.", false),
//                crearProducto("Bolígrafos metálicos", 7.99, "https://dummyimage.com/200x200/fff/aaa", false, false, false, null, 80, "Bolígrafos elegantes con acabado metálico.", false),
//                crearProducto("Sacapuntas eléctrico", 12.49, "https://dummyimage.com/200x200/fff/aaa", false, false, true, 10, 0, "Sacapuntas rápido y eficiente para uso diario.", false),
//                crearProducto("Notas adhesivas de colores", 6.99, "https://dummyimage.com/200x200/fff/aaa", false, false, false, null, 70, "Notas adhesivas en colores llamativos.", false),
//                crearProducto("Regla flexible 30 cm", 2.99, "https://dummyimage.com/200x200/fff/aaa", false, false, false, null, 90, "Regla flexible y resistente para todo uso.", false),
//
//                // Productos Favoritos
//                crearProducto("Set de acuarelas", 14.99, "https://dummyimage.com/200x200/fff/aaa", true, false, true, 60, 15, "Acuarelas de alta calidad para artistas creativos.", true),
//                crearProducto("Cinta adhesiva doble cara", 3.49, "https://dummyimage.com/200x200/fff/aaa", false, true, false, null, 60, "Cinta adhesiva perfecta para proyectos manuales.", true),
//                crearProducto("Rotuladores fluorescentes", 9.50, "https://dummyimage.com/200x200/fff/aaa", true, false, false, null, 0, "Rotuladores brillantes para destacar tus textos.", true),
//                crearProducto("Organizador para escritorio", 11.49, "https://dummyimage.com/200x200/fff/aaa", false, false, true, 5, 20, "Organizador práctico para mantener tu escritorio ordenado.", true),
//                crearProducto("Caja de clips metálicos", 4.99, "https://dummyimage.com/200x200/fff/aaa", false, false, false, null, 100, "Clips resistentes para documentos importantes.", true)
//            );
//
//            productoRepo.saveAll(productos);
//        };
//    }
 
//    private Producto crearProducto(String nombre, double precio, String imagen,
//                                   boolean lettering, boolean scrapbooking, boolean oferta,
//                                   Integer descuento, int cantidad, String descripcion, boolean favorito) {
//        Double precioOriginal = (descuento != null && descuento > 0)
//            ? precio / (1 - (descuento / 100.0))
//            : precio;
//
//        return new Producto(null, nombre, descripcion, precio, imagen, lettering, scrapbooking,
//                            oferta, descuento, cantidad, favorito, precioOriginal);
//    }
 
 
}