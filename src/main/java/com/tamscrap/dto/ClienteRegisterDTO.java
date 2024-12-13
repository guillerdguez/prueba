package com.tamscrap.dto;

import java.util.List;
import java.util.Set;

public class ClienteRegisterDTO {
    private String username;
    private String nombre;
    private String email;
    private String password;
    private List<String> authorities;
    private Set<Long> favoritosIds; // IDs de productos favoritos
    private Long carritoId; // Opcional, si el carrito se crea automáticamente

    // Getters y Setters

    public ClienteRegisterDTO() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getAuthorities() {
        return authorities;
    }
    
    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public Set<Long> getFavoritosIds() {
        return favoritosIds;
    }

    public void setFavoritosIds(Set<Long> favoritosIds) {
        this.favoritosIds = favoritosIds;
    }

    public Long getCarritoId() {
        return carritoId;
    }

    public void setCarritoId(Long carritoId) {
        this.carritoId = carritoId;
    }
}
