package com.equipo3.reuneme.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PasswordResetToken {

    private static final int DURACION_MINUTOS = 15; // Duración de 15 minutos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token; // Token seguro generado

    @Column(nullable = false)
    private String email; // El email del usuario asociado

    @Column(nullable = false)
    private LocalDateTime expirationTime; // Tiempo de expiración del token

    public PasswordResetToken() {}

    // Constructor para crear un nuevo token
    public PasswordResetToken(String token, String email) {
        this.token = token;
        this.email = email;
        this.expirationTime = LocalDateTime.now().plusMinutes(DURACION_MINUTOS); // Expira en 15 minutos
    }

    // Método para verificar si el token ha expirado
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expirationTime);
    }

    // Getters y setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }
}

