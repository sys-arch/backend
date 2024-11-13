package com.equipo3.reuneme.service;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.dao.AdministradorDAO;
import com.equipo3.reuneme.dao.EmpleadoDAO;
import com.equipo3.reuneme.dao.UsuarioDAO;
import com.equipo3.reuneme.model.Administrador;
import com.equipo3.reuneme.model.Empleado;
import com.equipo3.reuneme.model.Token;
import com.equipo3.reuneme.model.Usuario;

@Service
public class UsuarioService {
	@Autowired
	protected UsuarioDAO userdao;
	@Autowired
	protected EmpleadoDAO empdao;
	@Autowired
	protected AdministradorDAO admindao;
	@Autowired
	protected TokenService tokenService;
	@Autowired
	private TwoFactorAuthService twoFactorAuthService;
	
	/////////////////////////////////////
	//LOGIN GENERAL - EMPLEADOS Y ADMINS
	/////////////////////////////////////
	public boolean login(String email, String pwd) {
	    // Verificar si el usuario existe
	    Usuario u = this.userdao.findByEmail(email);
	    String errorMessage = "Credenciales incorrectas o desactivadas.";
	    if (Objects.isNull(u)) {
	        throw new ResponseStatusException(HttpStatus.FORBIDDEN, errorMessage);
	    }

	    // Verificar si la contraseña es correcta
	    if (!u.getPwd().equals(pwd)) {
	        throw new ResponseStatusException(HttpStatus.FORBIDDEN, errorMessage);
	    }

	    // Si el usuario es un Empleado, validar su estado
	    if (u instanceof Empleado) {
	        Empleado e = this.empdao.findByEmail(email);
	        // Comprobar si el empleado está bloqueado o no verificado
	        if (e.isBloqueado() || !e.isVerificado()) {
	            throw new ResponseStatusException(HttpStatus.FORBIDDEN, errorMessage);
	        }
	    }
	    // Si todo está correcto, devolver true para permitir acceso a la pantalla de doble autenticación
	    return true;
	}

	/////////////////////////////////////
	//OBTENER ROL DEL USUARIO
	/////////////////////////////////////
	public String getRoleByEmail(String email) {
        Usuario usuario = userdao.findByEmail(email);
        if (usuario != null) {
            return usuario.getRole(); 
        }
        return null;
    }


	
	/////////////////////////////////////
	//VER SI EL EMPLEADO ESTÁ BLOQUEADO
	/////////////////////////////////////
	public String findActivo(Map<String, Object> info) {
		Empleado e = this.empdao.findByEmail(info.get("email").toString());
		return String.valueOf(e.isBloqueado());
	}
	
	/////////////////////////////////////
	//LOGIN GENERAL - EMPLEADOS Y ADMINS
	/////////////////////////////////////
	public void registrar(Empleado user) {
	    // Comprobamos que el usuario no existe en la base de datos por email
	    Empleado userdb = this.empdao.findByEmail(user.getEmail());
	    if (userdb != null) {
	        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "El usuario ya existe en la base de datos.");
	    }
	    
//	    // Hashear la contraseña y guardar el nuevo usuario en la base de datos
	    user.setPwd(org.apache.commons.codec.digest.DigestUtils.sha512Hex(user.getPwd()));
	    user.setTwoFA(true);
	    this.empdao.save(user);
	}
	
	/////////////////////////////////////
	//BUSQUEDA USUARIOS (SOLO PARA PWD RESET)
	/////////////////////////////////////
	public Usuario findByEmail(String email) {
        Usuario usuario = userdao.findByEmail(email);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado.");
        }
        return usuario;
    }
	
	
	/////////////////////////////////////
	//GUARDA LA CLAVE SECRETA DE USUARIO POR PRIMERA VEZ
	/////////////////////////////////////
	public String activar2FA(String email) {
	    Usuario usuario = this.userdao.findByEmail(email);
	    if (usuario == null) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado.");
	    }
	    String secretKey = twoFactorAuthService.generateSecretKey();
	    usuario.setClavesecreta(secretKey);
	    usuario.setTwoFA(true);
	    userdao.save(usuario); 

	    return secretKey; 
	}
	
	/////////////////////////////////////
	//VERIFICA CODIGO DE GOOGLE AUTHENTICATOR
	/////////////////////////////////////
	public String verificarTwoFactorAuthCode(String email, Integer authCode) {
	    Usuario usuario = this.userdao.findByEmail(email);
	    if (usuario == null) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado.");
	    }
	    if (authCode == null) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código de autenticación de dos factores requerido.");
	    }
	    
	    // Validar el código de 2FA usando el servicio de 2FA
	    if (twoFactorAuthService.verifyCode(usuario.getClavesecreta(), authCode)) {
	        // Autenticación 2FA exitosa, generamos el JWT con el rol del usuario
	        String role = usuario.getRole();
	        return tokenService.generarToken(usuario.getEmail(), role); // Devuelve el JWT
	    } else {
	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Código de autenticación incorrecto.");
	    }
	}

	
}