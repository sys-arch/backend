package com.equipo3.reuneme.controller;

import com.equipo3.reuneme.service.EmailService;
import com.equipo3.reuneme.model.PasswordResetToken;
import com.equipo3.reuneme.model.Usuario;
import com.equipo3.reuneme.service.PasswordResetTokenService;
import com.equipo3.reuneme.service.PasswordService;
import com.equipo3.reuneme.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RestController
@RequestMapping("/pwd")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class PasswordResetController {

    @Autowired
    private PasswordResetTokenService tokenService;

    @Autowired
    private UsuarioService uservice;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private PasswordService passwordService;
    
    @Value("${APP_URL}")
    private String appUrl;

    // Endpoint para solicitar el token de recuperación de contraseña
    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
   	
    	//Verificar el email correctamente
        // Comprobamos que el email tiene un formato válido
        if (!emailService.validarEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email insertado no tiene un formato válido: "
            		+ "usuario@dominio.com");
        }
    	
        // Verificar que el usuario con ese email existe
        
        Usuario u = uservice.findByEmail(email);
        if (Objects.isNull(u)) {
            return ResponseEntity.badRequest().body("Empleado no existe");

        }

        // Generar el token de recuperación
        String token = tokenService.createPasswordResetToken(email);
        String dominio = appUrl;
        String resetLink = dominio + "/reset-contrasena?token=" + token;
       
        System.out.println("Link de APP_URL: " + resetLink); 
        
        String asunto = "Recuperación de contraseña de tu cuenta ReuneMe";
        String mensaje = "<p>Hola,</p>"
                + "<p>Hemos recibido una solicitud para restablecer tu contraseña. "
                + "Para continuar con el proceso, haz clic en el enlace a continuación:</p>"
                + "<a href=\"" + resetLink + "\">Restablecer contraseña</a>"
                + "<p>Si no has solicitado este cambio, puedes ignorar este correo.</p>";
        emailService.enviarEmail(email, asunto, mensaje);

        return ResponseEntity.ok("Se ha enviado un enlace para recuperar la contraseña a tu email. Token: "+token);

    }

    // Endpoint para validar el token cuando el usuario hace clic en el enlace de recuperación
    @GetMapping("/reset")

    public ResponseEntity<Map<String, String>> validateResetToken(@RequestParam String token) {
        Optional<PasswordResetToken> resetToken = tokenService.validatePasswordResetToken(token);

        if (resetToken.isPresent()) {
            // Obtener el email asociado al token
            String email = resetToken.get().getEmail();
            
            // Devolver un mapa con la validación del token y el email
            Map<String, String> response = new HashMap<>();
            response.put("message", "Token válido.");
            response.put("email", email);
            
            return ResponseEntity.ok(response);
        } else {
            // Si el token no es válido o ha caducado
            Map<String, String> response = new HashMap<>();
            response.put("message", "Token inválido o ha caducado.");
            return ResponseEntity.badRequest().body(response);
        }
    }
   
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");

        // Validación de la contraseña
        if (!passwordService.isSamePwd(newPassword, confirmPassword)) {
            return ResponseEntity.badRequest().body("Las contraseñas no coinciden.");
        }
        if (!passwordService.isValid(newPassword)) {
            return ResponseEntity.badRequest().body("La contraseña no cumple los requisitos de seguridad.");
        }

        try {
            tokenService.resetPassword(token, newPassword); // Restablece la contraseña si es válida
            return ResponseEntity.ok("¡Contraseña restablecida con éxito!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

