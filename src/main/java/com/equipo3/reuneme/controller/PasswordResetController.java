package com.equipo3.reuneme.controller;

import com.equipo3.reuneme.service.EmailService;
import com.equipo3.reuneme.service.PasswordResetTokenService;
import com.equipo3.reuneme.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/password")
public class PasswordResetController {

    @Autowired
    private PasswordResetTokenService tokenService;

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private EmailService eservice;

    // Endpoint para solicitar el token de recuperación de contraseña
    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
    	
    	//Verificar el email correctamente
        // Comprobamos que el email tiene un formato válido
        if (!eservice.validarEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email insertado no tiene un formato válido: "
            		+ "usuario@dominio.com");
        }
    	
        // Verificar que el usuario con ese email existe
        if (usuarioService.getEmpleado(email) == null) {
            return ResponseEntity.badRequest().body("No se ha podido realizar la petición");
        }

        // Generar el token de recuperación
        String token = tokenService.createPasswordResetToken(email);
        
        String dominio = System.getenv("APP_URL");
        
        String link = dominio + "/reset-password?token=" + token;
        String content = String.format("""
        	<h1>Reuneme</h1>
        	<p>Haga click en el siguiente enlace para <mark><b>cambiar la contraseña</b></mark> de su cuenta</p>
        	<a href="%s">%s</a>
        	Si no lo ha solicitado, no pulse el enlace y comuniquelo al administrador del sistema.
        	    """, link, link);

        eservice.enviarEmail(email, "Recuperación de Contraseña", content);

        return ResponseEntity.ok("Si su cuenta existe en el sistema, recibirá un email con instrucciones");
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