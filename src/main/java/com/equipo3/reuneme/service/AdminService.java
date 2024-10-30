package com.equipo3.reuneme.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.dao.AdministradorDAO;
import com.equipo3.reuneme.dao.AusenciaDAO;
import com.equipo3.reuneme.dao.EmpleadoDAO;
import com.equipo3.reuneme.dao.UsuarioDAO;
import com.equipo3.reuneme.model.Administrador;
import com.equipo3.reuneme.model.Ausencia;
import com.equipo3.reuneme.model.Empleado;
import com.equipo3.reuneme.model.Usuario;

@Service
public class AdminService {
	
	@Autowired
	private EmpleadoDAO empdao;
	
	@Autowired
	private AdministradorDAO admindao;
	
	@Autowired
	private UsuarioDAO userdao;
	
	@Autowired
	private AusenciaDAO adao;
	
	/////////////////////////
	//REGISTRO ADMINISTRADOR
	////////////////////////
	public void registrarAdmin(Administrador admin) {
		//Comprobar que el email no está ya registrado
		Administrador a = this.admindao.findByEmail(admin.getEmail());
		if (a != null) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"El administrador ya existe en la base de datos");
		}
		//Cifrar la contraseña
		admin.setPwd(org.apache.commons.codec.digest.DigestUtils.sha512Hex(admin.getPwd()));
		this.admindao.save(admin);
	}
	
	///////////////////////////
	//ACTUALIZAR EMPLEADO
	///////////////////////////
	public void actualizarEmpleado(String email, Empleado empleadoActualizado) {
		Empleado empleadoExistente = this.empdao.findByEmail(email);
		if(Objects.isNull(empleadoExistente)) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No existe el empleado seleccionado");
		}
		
		empleadoExistente.setNombre(empleadoActualizado.getNombre());
        empleadoExistente.setApellido1(empleadoActualizado.getApellido1());
        empleadoExistente.setApellido2(empleadoActualizado.getApellido2());
        empleadoExistente.setDepartamento(empleadoActualizado.getDepartamento());
        empleadoExistente.setPerfil(empleadoActualizado.getPerfil());
        empleadoExistente.setCentro(empleadoActualizado.getCentro());
        empleadoExistente.setBloqueado(empleadoActualizado.isBloqueado());
        empleadoExistente.setVerificado(empleadoActualizado.isVerificado());
        
        this.empdao.save(empleadoExistente);

	}

	//////////////////////////
	//VERIFICAR EMPLEADO
	/////////////////////////
	public void verificarEmpleado(String email) {
		Empleado emp = this.empdao.findByEmail(email);
		
		if(Objects.isNull(emp)) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No existe el empleado seleccionado");
		}
		
		if(emp.isVerificado()) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "El empleado ya está verificado");
		}
		
		this.empdao.deleteById(emp.getId());
		
		emp.setVerificado(true);
		emp.setBloqueado(false);
		
		this.empdao.save(emp);
		
	}
	
	//////////////////////////
	// BORRAR EMPLEADO
	//////////////////////////
	public void deleteUser(String email) {
	    Empleado empleado = this.empdao.findByEmail(email);
	    if (empleado != null) {
	        this.empdao.deleteById(empleado.getId());
	        return;
	    }

	    Administrador administrador = this.admindao.findByEmail(email);
	    if (administrador != null) {
	        this.admindao.deleteById(administrador.getId());
	        return;
	    }

	    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con el email especificado no existe");
	}

	//////////////////////////
	//BLOQUEAR/DESBLOQUEAR EMPLEADO
	//////////////////////////
	public void cambiarEstadoBloqueoEmpleado(String email, boolean bloquear) {
        Empleado empleado = this.empdao.findByEmail(email);

        if (Objects.isNull(empleado)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado");
        }

        if (empleado.isBloqueado() == bloquear) {
            String estado = bloquear ? "bloqueado" : "desbloqueado";
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "El empleado ya está " + estado);
        }

        empleado.setBloqueado(bloquear);
        this.empdao.save(empleado);
    }

	
	//////////////////////////
	// LISTA EMPLEADOS
	//////////////////////////
	public List<Empleado> getEmpleados() {
		List<Empleado> lista = this.empdao.findAll();
		
		if(lista.isEmpty() || Objects.isNull(lista)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No hay empleados registrados");
		}
		
		return lista;
	}
	
	//////////////////////////
	// GET INFO EMPLEADO
	//////////////////////////
	public Empleado getEmpleado(String email) {
		
		Empleado e = this.empdao.findByEmail(email);
		
		if (Objects.isNull(e)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No existe el usuario");
		}
		
		return e;
	}
	
	
	//////////////////////////
	// CONSULTAR AUSENCIAS DE UN USUARIO
	//////////////////////////
	public List<Ausencia> consultarAusencias (String email) {
		
		Usuario u = this.userdao.findByEmail(email);
		
		if (Objects.isNull(u)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No existe el usuario que intentas consultar");
		} else {
			List<Ausencia> lista = this.adao.findByUsuario(u);
			
			return lista;
		
		}

	}
	
	//////////////////////////
	// AÑADIR AUSENCIA A UN USUARIO
	//////////////////////////
	public void anadirAusencias (String email, Ausencia au) {
		
		Usuario u = this.userdao.findByEmail(email);
		
		if (Objects.isNull(u)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No existe el usuario");
		} else {
			au.setUsuario(u);
			this.adao.save(au);
		}

	}

    /*********************************
     *DEVOLVER TODOS LOS USUARIOS
     ********************************/
	public List<Usuario> obtenerTodosLosUsuarios() {
		return userdao.findAll();

	}
	
	//////////////////////////
	//VER DATOS DEL ADMINISTRADOR
	//////////////////////////
	public Administrador verDatos(String email) {
	Administrador administrador = this.admindao.findByEmail(email);
	if (administrador == null) {
	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Administrador no encontrado");
	}
	return administrador;
	}

	
}
