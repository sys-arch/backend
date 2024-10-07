package com.equipo3.reuneme.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.dao.UsuarioDAO;
import com.equipo3.reuneme.model.Usuario;



@Service
public class UserService {

    @Autowired
    private UsuarioDAO userDao;
    
    public void registrar(Usuario user) {
        Usuario userdb = this.userDao.findByEmailAndPwd(user.getEmail(), user.getPwd());
        if (userdb == null) {
        	throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No se ha podido registrar el usuario");
        }
        String pass = user.getPwd();
        String hash = "";
        try {
            hash = hashearPass(pass);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"No se ha podido hashear la contraseña");
        }
        user.setPwd(hash);
        this.userDao.save(user);
    }
    
    public String hashearPass(String cadena) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bytes = cadena.getBytes(StandardCharsets.UTF_8);
        byte[] hash = md.digest(bytes);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            sb.append(Integer.toHexString(0xFF & hash[i]));
        }
        return sb.toString();
    }

}
