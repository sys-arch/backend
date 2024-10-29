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
	
	/////////////////////////////////////
	//LOGIN GENERAL - EMPLEADOS Y ADMINS
	/////////////////////////////////////
	public String login(String email, String pwd) {
		
		//¿Existe el usuario?
		Usuario u = this.userdao.findByEmail(email);
		if (Objects.isNull(u)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, 
					"El usuario no existe o las credenciales son incorrectas.");
		}
		
		//¿La contraseña es correcta?
		if(!u.getPwd().equals(pwd)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales inválidas.");
		}
			
		String pretoken;
		String idToken  = UUID.randomUUID().toString();
		Token token = new Token();
		token.setId(idToken);
		token.setEmail(u.getEmail());
		token.setUsuario(u);
		
		
		if (u instanceof Administrador) {
			pretoken = "a-";
			return pretoken + this.tokenService.generarToken(token);
        } else {
			pretoken = "e-";
			Empleado e = this.empdao.findByEmail(email);
			if(e.isBloqueado()) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario bloqueado");
			}
			return pretoken + this.tokenService.generarToken(token);
        }
		
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


}