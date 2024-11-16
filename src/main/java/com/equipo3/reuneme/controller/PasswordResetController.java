package com.equipo3.reuneme.controller;

import com.equipo3.reuneme.service.AdminService;
import com.equipo3.reuneme.service.EmailService;
import com.equipo3.reuneme.model.Empleado;
import com.equipo3.reuneme.model.Usuario;
import com.equipo3.reuneme.service.PasswordService;
import com.equipo3.reuneme.service.TokenService;
import com.equipo3.reuneme.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/pwd")
public class PasswordResetController {

    @Autowired
    private TokenService tokenService; // Usa TokenService en lugar de PasswordResetTokenService

    @Autowired
    private UsuarioService uservice;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private AdminService aservice;
    
    @Autowired
    private PasswordService passwordService;
    
    @Value("${APP_URL}")
    private String appUrl;

    // Endpoint para solicitar el token de recuperación de contraseña
    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        email = email.toLowerCase();

        // Validar el formato del email
        if (!emailService.validarEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email insertado no tiene un formato válido: usuario@dominio.com");
        }

        // Verificar que el usuario con ese email existe
        Usuario u = uservice.findByEmail(email);
        if (Objects.isNull(u)) {
            return ResponseEntity.badRequest().body("El usuario que buscas no existe");
        }

        // Verificar si el usuario es un empleado bloqueado
        if (u instanceof Empleado) {
            Empleado e = aservice.getEmpleado(email);
            if (e.isBloqueado()) {
                return ResponseEntity.status(403).body("El usuario que buscas no existe");
            }
        }

        // Generar el token de recuperación (usando TokenService)
        String token = tokenService.generarToken(email, "RESET_PASSWORD");
        String resetLink = appUrl + "/reset-contrasena?token=" + token;

        // Enviar email con el enlace de recuperación
        String asunto = "Recuperación de contraseña de tu cuenta ReuneMe";
        String mensaje = "<p>Hola,</p>"
                + "<p>Hemos recibido una solicitud para restablecer tu contraseña. "
                + "Para continuar con el proceso, haz clic en el enlace a continuación:</p>"
                + "<a href=\"" + resetLink + "\">Restablecer contraseña</a>"
                + "<p>Si no has solicitado este cambio, puedes ignorar este correo.</p>";
        emailService.enviarEmail(email, asunto, mensaje);

        return ResponseEntity.ok("Se ha enviado un enlace para recuperar la contraseña a tu email. Token: " + token);
    }

    // Endpoint para validar el token cuando el usuario hace clic en el enlace de recuperación
    @GetMapping("/reset")
    public ResponseEntity<Map<String, String>> validateResetToken(@RequestParam String token) {
        System.out.println("Solicitud recibida para validar token: " + token);
        try {
            tokenService.validarToken(token);
            String email = tokenService.obtenerEmail(token);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Token válido.");
            response.put("email", email);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Token inválido o ha caducado.");
            return ResponseEntity.status(e.getStatusCode()).body(response);
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
            // Validar el token
            tokenService.validarToken(token);

            // Obtener el email asociado al token
            String email = tokenService.obtenerEmail(token);

            // Restablecer la contraseña
            uservice.updatePassword(email, newPassword);

            return ResponseEntity.ok("¡Contraseña restablecida con éxito!");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }
}


