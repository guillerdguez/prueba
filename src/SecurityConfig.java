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
				.requestMatchers("/api/producto/listar", "/api/producto/buscar/**", "/api/producto/ver/**",
						"/api/producto/categoria/**", "/api/auth/**", "/home", "/carrito", "/api/pedidos/addPedido",
						"/", "/api/clientes/addCliente","/register")
				.permitAll().requestMatchers("/api/carrito/editar/**").authenticated()
				.requestMatchers("/api/pedidos/delete/**","/api/pedidos/editar/**").hasAnyAuthority("USER", "ADMIN")
				.requestMatchers("/api/pedidos/**", "/profile/**").authenticated()
				.requestMatchers("/api/producto/addProducto", "/api/producto/editar/**", "/api/producto/borrar/**")
				.hasAuthority("ADMIN").anyRequest().authenticated())
				.exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

 
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		String[] publicEndpoints = { "/api/producto/listar", "/api/producto/buscar/**", "/api/producto/ver/**",
//				"/api/producto/categoria/**", "/api/auth/**", "/home", "/carrito", "/api/pedidos/addPedido", "/",
//				"/api/clientes/addCliente", "/register",
//
//				"/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/webjars/**" };
//
//		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth.requestMatchers(publicEndpoints)
//				.permitAll().requestMatchers("/api/carrito/editar/**").authenticated()
//				.requestMatchers("/api/pedidos/delete/**", "/api/pedidos/editar/**").hasAuthority("ADMIN")
//				.requestMatchers("/api/pedidos/editarEstado/**").hasAuthority("ADMIN")
//				.requestMatchers("/api/pedidos/**", "/profile/**").authenticated()
//				.requestMatchers("/api/producto/addProducto", "/api/producto/editar/**", "/api/producto/borrar/**")
//				.hasAuthority("ADMIN").anyRequest().authenticated())
//				.exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
//				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//
//		return http.build();
//	}

	@Configuration
	public class CorsConfig implements WebMvcConfigurer {
		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**").allowedOrigins("http://localhost:4200", "https://tamscrapt.up.railway.app")
					.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedHeaders("*")
					.exposedHeaders("Authorization").allowCredentials(true);
		}
	}

}
