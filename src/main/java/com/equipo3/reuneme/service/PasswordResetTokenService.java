package com.equipo3.reuneme.service;

import com.equipo3.reuneme.dao.PasswordResetTokenRepository;
import com.equipo3.reuneme.dao.UsuarioDAO;
import com.equipo3.reuneme.model.PasswordResetToken;
import com.equipo3.reuneme.model.Usuario;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

import java.util.UUID;

@Service
public class PasswordResetTokenService {

    @Autowired

    private PasswordResetTokenDAO tokenrepo;

    @Autowired
    private UsuarioDAO usuarioDAO;


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

    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> resetToken = tokenrepo.findByToken(token);
        
        if (resetToken.isEmpty() || resetToken.get().isExpired()) {
            throw new IllegalArgumentException("Token inválido o caducado.");
        }

        // Obtener el email asociado al token
        String email = resetToken.get().getEmail();
        Usuario usuario = usuarioDAO.findByEmail(email);

        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        // Encriptar la nueva contraseña usando SHA-512
        usuario.setPwd(hashPassword(newPassword));
        usuarioDAO.save(usuario);

        // Eliminar el token una vez utilizado
        tokenrepo.deleteByToken(token);

        return true;
    }

    // Método para encriptar la contraseña usando SHA-512
    private String hashPassword(String password) {
        return DigestUtils.sha512Hex(password);
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

