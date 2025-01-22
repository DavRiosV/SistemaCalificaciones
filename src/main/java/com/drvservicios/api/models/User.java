package com.drvservicios.api.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users") // Asegúrate de que esta tabla existe en tu base de datos
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Nombre del usuario

    @Column(unique = true, nullable = false)
    private String username; // Nombre de usuario único

    @Column(unique = true, nullable = false)
    private String email; // Email único

    @Column(nullable = false)
    private String password; // Contraseña codificada

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "roles")
    private List<Role> roles; // Lista de roles asociados al usuario

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    // Métodos de UserDetails para Spring Security
    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Personaliza según tu lógica
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Personaliza según tu lógica
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Personaliza según tu lógica
    }

    @Override
    public boolean isEnabled() {
        return true; // Personaliza según tu lógica
    }
}
