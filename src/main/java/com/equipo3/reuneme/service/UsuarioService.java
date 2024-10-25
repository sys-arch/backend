package com.equipo3.reuneme.service;

import java.util.List;
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
		
		if(u instanceof Empleado) {
			Empleado e = this.empdao.findByEmail(u.getEmail());
			
			if(e.isBloqueado()) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario bloqueado");
			}
		}
			
		String pretoken;
		String idToken  = UUID.randomUUID().toString();
		Token token = new Token(idToken, u);
		
		if (u instanceof Administrador) {
			pretoken = "a-";
			return pretoken + this.tokenService.generarToken(token);
        } else {
			pretoken = "e-";
			return pretoken + this.tokenService.generarToken(token);
        }
		
	}
	
	public String findActivo(Map<String, Object> info) {
		Empleado e = this.empdao.findByEmail(info.get("email").toString());
		return String.valueOf(e.isBloqueado());
	}
	
	
	public void registrar(Empleado user) {
	    // Comprobamos que el usuario no existe en la base de datos por email
	    Empleado userdb = this.empdao.findByEmail(user.getEmail());
	    if (userdb != null) {
	        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "El usuario ya existe en la base de datos.");
	    }
	    
//	    // Hashear la contraseña y guardar el nuevo usuario en la base de datos
//	    user.setPwd(org.apache.commons.codec.digest.DigestUtils.sha512Hex(user.getPwd()));
	    this.empdao.save(user);
	}
	public Usuario findByEmail(String email) {
        Usuario usuario = userdao.findByEmail(email);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado.");
        }
        return usuario;
    }

	public void registrarAdmin(Administrador admin) {
		//Comprobar que no existe un usuario con este email
		Usuario userdb = this.userdao.findByEmail(admin.getEmail());
		if (userdb != null) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"El administrador ya existe en la base de datos");
		}
		//Cifrar la contraseña
		admin.setPwd(org.apache.commons.codec.digest.DigestUtils.sha512Hex(admin.getPwd()));
		this.admindao.save(admin);
	}


	
	public void delete(String email) {
		Usuario u = this.userdao.findByEmail(email);
		if (Objects.isNull(u)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Error al borrar el usuario");
		}
		
		if (u instanceof Empleado) {
			empdao.deleteById(u.getId());
		} else {
			admindao.deleteById(u.getId());
		}

	}

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

	public List<Empleado> getEmpleados() {
		List<Empleado> lista = this.empdao.findAll();
		
		if(lista.isEmpty() || Objects.isNull(lista)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No hay empleados registrados");
		}
		
		return lista;
	}

	public Empleado getEmpleado(String email) {
		
		Empleado e = this.empdao.findByEmail(email);
		
		if (Objects.isNull(e)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No existe el usuario");
		}
		
		return e;
	}
	
	public void actualizar(Empleado e) {
		
		Empleado emp = this.empdao.findByEmail(e.getEmail());
		if (Objects.isNull(emp)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No existe el usuario que intentas borrar");
		}
			
		delete(emp.getEmail());
			
		this.empdao.save(emp);		
		

	}
	


}