package com.equipo3.reuneme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.security.JwtTokenProvider;

@Service
public class TokenService {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // Genera un JWT usando JwtTokenProvider
    public String generarToken(String email, String role) {
        return jwtTokenProvider.generateToken(email, role);
    }

    // Valida el JWT usando JwtTokenProvider
    public void validarToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token no válido o ha expirado.");
        }
    }

    // Obtiene el email del usuario desde el JWT
    public String obtenerEmail(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token no válido o ha expirado.");
        }
        return jwtTokenProvider.getUsernameFromToken(token);
    }
    
    // Obtiene el rol del usuario desde el JWT
    public String obtenerRole(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token no válido o ha expirado.");
        }
        return jwtTokenProvider.getRoleFromToken(token);
    }
}
