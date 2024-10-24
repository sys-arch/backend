package com.equipo3.reuneme.service;

import com.equipo3.reuneme.dao.PasswordResetTokenDAO;
import com.equipo3.reuneme.model.PasswordResetToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenDAO tokenrepo;

    // Generar un token seguro para la recuperación de contraseña
    public String createPasswordResetToken(String email) {
        // Eliminar cualquier token previo asociado al mismo email
        tokenrepo.deleteByEmail(email);

        // Generar un nuevo token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, email);
        
        // Guardar el token en la base de datos
        tokenrepo.save(resetToken);

        return token;
    }

    // Validar si el token es válido (existe y no ha expirado)
    public Optional<PasswordResetToken> validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> resetTokenOpt = tokenrepo.findByToken(token);
        
        // Comprobar si el token existe y no ha caducado
        if (resetTokenOpt.isPresent() && !resetTokenOpt.get().isExpired()) {
            return resetTokenOpt;
        } else {
            return Optional.empty(); // Token inválido o caducado
        }
    }

    // Eliminar un token después de usarlo o si ha caducado
    public void deleteToken(String token) {
        tokenrepo.deleteByToken(token);
    }
}