package com.equipo3.reuneme.controller;

import com.equipo3.reuneme.model.PasswordResetToken;
import com.equipo3.reuneme.service.PasswordResetTokenService;
import com.equipo3.reuneme.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password")
public class PasswordResetController {

    @Autowired
    private PasswordResetTokenService tokenService;

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint para solicitar el token de recuperación de contraseña
    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        // Verificar que el usuario con ese email existe
        if (usuarioService.findByEmail(email) == null) {
            return ResponseEntity.badRequest().body("No existe ningún usuario con ese email.");
        }

        // Generar el token de recuperación
        String token = tokenService.createPasswordResetToken(email);
        String resetLink = "https://tuapp.com/reset-password?token=" + token; // URL de recuperación

        // Enviar el enlace por email (no implementado aquí, pero se puede añadir)
        // sendResetLinkToEmail(email, resetLink);

        return ResponseEntity.ok("Se ha enviado un enlace para recuperar la contraseña a tu email. Token: "+token);
    }

    // Endpoint para validar el token cuando el usuario hace clic en el enlace de recuperación
    @GetMapping("/reset")
    public ResponseEntity<String> validateResetToken(@RequestParam String token) {
        // Validar el token
        if (tokenService.validatePasswordResetToken(token).isPresent()) {
            return ResponseEntity.ok("Token válido. Procede a restablecer tu contraseña.");
        } else {
            return ResponseEntity.badRequest().body("Token inválido o ha caducado.");
        }
    }
}
