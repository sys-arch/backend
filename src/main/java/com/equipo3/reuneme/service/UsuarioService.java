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
	
	protected UsuarioDAO userdao;
	protected EmpleadoDAO empdao;
	protected AdministradorDAO admindao;
	protected TokenService tokenService;
	
	@Autowired
	public String login(Map<String, Object> info) {
		String email = info.get("email").toString();
		String password = org.apache.commons.codec.digest.DigestUtils.sha512Hex(info.get("contrasena").toString());
		
		//¿Existe el usuario?
		Usuario u = this.userdao.findByEmail(email);
		if (Objects.isNull(u)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Las credenciales son incorrectas.");
		} else {
			//¿La contraseña es correcta?
			if(u.getPwd().compareTo(password) != 0) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Estas credenciales son incorrectas.");
			}
			
			String pretoken;
			String idToken  = UUID.randomUUID().toString();
			Token token;
			
			Empleado e = this.empdao.findByEmail(email);
			Administrador a = this.admindao.findByEmail(email);
			
			if(Objects.isNull(e)) {
				//Si Empleado es null entonces es que es Administrador
				pretoken = "a-";
				token = new Token(idToken, a);
				//se devuelve prefijo + token para identificar tipo
				return pretoken + this.tokenService.generarToken(token);
			} else {
				pretoken = "e-";
				token = new Token(idToken, e);
				return pretoken + this.tokenService.generarToken(token);
			}
			
		}
		
	}
	
	@Autowired
	public String findActivo(Map<String, Object> info) {
		Empleado e = this.empdao.findByEmail(info.get("email").toString());
		return String.valueOf(e.isBloqueado());
	}
	
	@Autowired
	public void registrar(Empleado user) {
    	
    	//Antes de registar, comprobamos que usuario existe
        Empleado userdb = this.empdao.findByEmailAndPwd(user.getEmail(), user.getPwd());
        if (userdb == null) {
        	throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No se ha podido registrar el usuario");
        }
        
        //Como no existe, pwd se hashea a SHA512. se guarda y se mete el usuario en BD
        user.setPwd(org.apache.commons.codec.digest.DigestUtils.sha512Hex(user.getPwd()));
        this.empdao.save(user);
    }

	@Autowired
	public void delete(String email) {
		Usuario u = this.userdao.findByEmail(email);
		if (Objects.isNull(u)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Error al borrar el usuario");
		}
		
		userdao.delete(u);
		
	}

  @Autowired
	public void bloquear(Map<String, Object>info) {
		String email = info.get("email").toString();
		Boolean bloqueado = Boolean.parseBoolean(info.get("contrasena").toString());
		
		Empleado e = this.empdao.findByEmail(email);
		if (Objects.isNull(e)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No existe el usuario que intentas borrar");
		} else {
			if(e.isBloqueado() == bloqueado) {
				throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "El usuario ya está en el estado peticionado");
			}
			
			delete(email);
			
			e.setBloqueado(bloqueado);
			empdao.save(e);
		}
		
		
		
	}


}