package com.tamscrap.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService  userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JwtRequestFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
			return;
		}

		final String requestTokenHeader = request.getHeader("Authorization");

		String username = null;
		String jwtToken = null;

		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7); 
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);

				logger.info("Token recibido: " + jwtToken);
				logger.info("Usuario del token: " + username);

			} catch (IllegalArgumentException e) {
				logger.error("No se pudo obtener el token JWT", e);
			} catch (ExpiredJwtException e) {
				logger.warn("El token JWT ha expirado", e);
			} catch (Exception e) {
				logger.error("Error al procesar el token JWT", e);
			}
		} else {
			if (requestTokenHeader != null) {
				logger.warn("JWT Token no comienza con 'Bearer '");
			} else {
				logger.debug("Encabezado de autorización no proporcionado");
			}
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userService.loadUserByUsername(username);

			if (jwtToken != null && jwtTokenUtil.validateToken(jwtToken, userDetails)) {
				logger.info("Token válido: " + jwtTokenUtil.validateToken(jwtToken, userDetails));

				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			} else {
				logger.warn("Token inválido o expirado para el usuario: {}", username);
			}
		}

		chain.doFilter(request, response);
	}

}
