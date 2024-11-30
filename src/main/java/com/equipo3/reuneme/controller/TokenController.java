package com.equipo3.reuneme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import com.equipo3.reuneme.service.TokenService;

@RestController
@RequestMapping("/tokens")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    // Endpoint para validar el JWT
    @GetMapping("/validarToken")
    public ResponseEntity<?> validarToken(@RequestHeader(name = "Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            tokenService.validarToken(token);
            return ResponseEntity.ok("Token válido");
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
        }
    }


    // Endpoint para obtener el email desde el JWT
    @GetMapping("/obtenerEmail")
    public String obtenerEmail(@RequestHeader(name = "Authorization") String authHeader) {
        String token = extractToken(authHeader);
        return tokenService.obtenerEmail(token);
    }

    // Método privado para extraer el token desde el encabezado Authorization
    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Remover el prefijo "Bearer "
        }
        throw new IllegalArgumentException("El encabezado Authorization no contiene un token JWT válido");
    }
}
