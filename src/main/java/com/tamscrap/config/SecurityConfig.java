package com.tamscrap.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

//    @Autowired
//    private UserServiceImpl userService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(
			org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration authConfig)
			throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
	            // Rutas públicas accesibles para todos
	            .requestMatchers(
	                "/api/producto/listar",  "/api/producto/buscar/**", 
	                "/api/producto/ver/**", 
	                "/api/producto/categoria/**",
	                "/api/auth/**", 
	                "/home", 
	                "/carrito",
	                "/api/pedidos/addPedido", 
	                "/", 
	                "/api/clientes/addCliente"  
	            ).permitAll()
	            // Rutas restringidas a usuarios autenticados
	            .requestMatchers("/api/pedidos/**", "/profile/**").authenticated()
	            // Rutas restringidas a administradores
	            .requestMatchers(
	                "/api/producto/addProducto", 
	                "/api/producto/editar/**", 
	                "/api/producto/borrar/**"
	            ).hasAuthority("ADMIN")
	            // Cualquier otra ruta requiere autenticación
	            .anyRequest().authenticated())
	        .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

	    // Agregar el filtro JWT antes del filtro de autenticación
	    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}

//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**").allowedOrigins("http://localhost:4200")
//						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedHeaders("*")
//						.exposedHeaders("Authorization").allowCredentials(true);
//			}
//		};
//
//	}
	@Configuration
	public class CorsConfig implements WebMvcConfigurer {
	    @Override
	    public void addCorsMappings(CorsRegistry registry) {
	        registry.addMapping("/**")
	                .allowedOrigins("http://localhost:4200")
	                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
	                .allowedHeaders("*")
	                .exposedHeaders("Authorization")
	                .allowCredentials(true);
	    }
	}

}
