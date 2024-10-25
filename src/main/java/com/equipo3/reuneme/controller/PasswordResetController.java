package com.equipo3.reuneme.controller;

import com.equipo3.reuneme.service.EmailService;
import com.equipo3.reuneme.model.PasswordResetToken;
import com.equipo3.reuneme.model.RegistroDatos; 
import com.equipo3.reuneme.service.PasswordResetTokenService;
import com.equipo3.reuneme.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/password")
public class PasswordResetController {

    @Autowired
    private PasswordResetTokenService tokenService;

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private EmailService emailService;

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
        String resetLink = "http://localhost:4200/reset-contrasena?token=" + token;
        
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

        // Validación de la contraseña con los requisitos de seguridad
        RegistroDatos datos = new RegistroDatos();
        datos.setPwd1(newPassword);
        datos.setPwd2(confirmPassword);
        try {
            datos.comprobarPwd();  // Verifica que las contraseñas cumplan los requisitos

            // Si pasa la validación, restablece la contraseña en la base de datos
            tokenService.resetPassword(token, newPassword);
            return ResponseEntity.ok("¡Contraseña restablecida con éxito!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}


