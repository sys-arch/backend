package com.equipo3.reuneme.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.dao.AdministradorDAO;
import com.equipo3.reuneme.dao.AusenciaDAO;
import com.equipo3.reuneme.dao.EmpleadoDAO;
import com.equipo3.reuneme.dao.TurnoDAO;
import com.equipo3.reuneme.dao.UsuarioDAO;
import com.equipo3.reuneme.model.Administrador;
import com.equipo3.reuneme.model.Ausencia;
import com.equipo3.reuneme.model.Empleado;
import com.equipo3.reuneme.model.RegistroAusencia;
import com.equipo3.reuneme.model.Reunion;
import com.equipo3.reuneme.model.Turno;
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

	@Autowired
	private TurnoDAO tdao;
	
	@Autowired
	private EmpleadoService eserv;

	/////////////////////////
	// REGISTRO ADMINISTRADOR
	////////////////////////
	public void registrarAdmin(Administrador admin) {
		// Comprobar que el email no está ya registrado
		Administrador a = this.admindao.findByEmail(admin.getEmail());
		if (a != null) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
					"El administrador ya existe en la base de datos");
		}
		// Cifrar la contraseña
		admin.setPwd(org.apache.commons.codec.digest.DigestUtils.sha512Hex(admin.getPwd()));
		admin.setTwoFA(true);
		this.admindao.save(admin);
	}


	//////////////////////////
	// VERIFICAR EMPLEADO
	/////////////////////////
	public void verificarEmpleado(String email) {
		Empleado emp = this.empdao.findByEmail(email);

		if (Objects.isNull(emp)) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No existe el empleado seleccionado");
		}

		if (emp.isVerificado()) {
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
	    // Buscar empleado por email
	    Empleado empleado = this.empdao.findByEmail(email);
	    if (empleado != null) {
	        // Obtener todas las ausencias asociadas al empleado
	        List<Ausencia> ausencias = this.adao.findAllByEmpleado(empleado);
	        // Eliminar todas las ausencias
	        this.adao.deleteAll(ausencias);
	        // Luego eliminar el empleado
	        this.empdao.deleteById(empleado.getId());
	        return; // Salir después de eliminar el empleado
	    }

	    // Buscar administrador por email (si no se encontró como empleado)
	    Administrador administrador = this.admindao.findByEmail(email);
	    if (administrador != null) {
	        this.admindao.deleteById(administrador.getId());
	        return; // Salir después de eliminar el administrador
	    }

	    // Lanzar excepción si no se encuentra ni empleado ni administrador
	    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario con el email especificado no existe");
	}



	//////////////////////////
	// BLOQUEAR/DESBLOQUEAR EMPLEADO
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

		if (lista.isEmpty() || Objects.isNull(lista)) {
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
	public List<Ausencia> consultarAusencias() {
		List<Ausencia> lista = this.adao.findAll();
		
		if (lista.isEmpty()) {
		throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay ausencias registradas");
		}
		
		return lista;
	}
	
	//////////////////////////
	// ELIMINAR AUSENCIA
	//////////////////////////
	public void eliminarAusencia(String ausenciaId) {
        if (!adao.existsById(ausenciaId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La ausencia no existe");
        }
        adao.deleteById(ausenciaId);
    }
	//////////////////////////
	// DEVOLVER TODAS LAS AUSENCIAS
	//////////////////////////
	public List<Ausencia> obtenerTodasLasAusencias() {
	List<Ausencia> lista = this.adao.findAll();
	
	if (lista.isEmpty()) {
	throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay ausencias registradas");
	}
	
	return lista;
	}

	//////////////////////////
	// AÑADIR AUSENCIA A UN USUARIO
	//////////////////////////
	public Ausencia anadirAusencias(String email, RegistroAusencia au) {

		Empleado emp = this.empdao.findByEmail(email);

		if (Objects.isNull(emp)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No existe el usuario");
		}
    
		Ausencia a = new Ausencia(au.getFechaInicio(), au.getFechaFin(), au.getMotivo(), emp);
		return this.adao.save(a);

	}

	//////////////////////////
	// DEVOLVER LISTA DE USUARIOS
	//////////////////////////
	public List<Usuario> obtenerTodosLosUsuarios() {
		return userdao.findAll();

	}

	//////////////////////////
	// VER DATOS DEL ADMINISTRADOR
	//////////////////////////
	public Administrador verDatos(String email) {
		Administrador administrador = this.admindao.findByEmail(email);
		if (administrador == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Administrador no encontrado");
		}
		return administrador;
	}

	//////////////////////////
	// AÑADIR TURNO
	//////////////////////////
	public void anadirTurnos(List<Turno> turnos) {
		
		if(this.tdao.count() != 0) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Ya existen turnos y no se pueden modificar");
		}
		
		this.tdao.saveAll(turnos);
	}

	//////////////////////////
	// DEVOLVER LISTA DE TURNOS
	//////////////////////////
	public List<Turno> turnos() {
		return this.tdao.findAll();
	}

	
	///////////////////////////
	// ACTUALIZAR ADMINISTRADOR
	///////////////////////////
	public void actualizarAdministrador(String email, Administrador administradorActualizado) {
	    Administrador administradorExistente = this.admindao.findByEmail(email);
	    if (Objects.isNull(administradorExistente)) {
	        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No existe el administrador seleccionado");
	    }

	    // Solo actualiza los campos si el valor en administradorActualizado no es null
	    if (administradorActualizado.getNombre() != null) {
	        administradorExistente.setNombre(administradorActualizado.getNombre());
	    }
	    if (administradorActualizado.getApellido1() != null) {
	        administradorExistente.setApellido1(administradorActualizado.getApellido1());
	    }
	    if (administradorActualizado.getApellido2() != null) {
	        administradorExistente.setApellido2(administradorActualizado.getApellido2());
	    }
	    if (administradorActualizado.getCentro() != null) {
	        administradorExistente.setCentro(administradorActualizado.getCentro());
	    }
	    if (administradorActualizado.isInterno() != null) { // si Interno es Boolean en la entidad
	        administradorExistente.setInterno(administradorActualizado.isInterno());
	    }
	    if (administradorActualizado.getTwoFA() != null) { // si TwoFA es Boolean en la entidad
	        administradorExistente.setTwoFA(administradorActualizado.getTwoFA());
	    }
	    if (administradorActualizado.getClavesecreta() != null) {
	        administradorExistente.setClavesecreta(administradorActualizado.getClavesecreta());
	    }

	    // Guarda los cambios
	    this.admindao.save(administradorExistente);
	}

	
	///////////////////////////
	// ACTUALIZAR EMPLEADO
	///////////////////////////
	public void actualizarEmpleado(String email, Empleado empleadoActualizado) {
	    Empleado empleadoExistente = this.empdao.findByEmail(email.toLowerCase());
	    
	    if (Objects.isNull(empleadoExistente)) {
	        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No existe el empleado seleccionado");
	    }

	    // Solo actualiza los campos si el valor en empleadoActualizado no es null
	    if (empleadoActualizado.getNombre() != null) {
	        empleadoExistente.setNombre(empleadoActualizado.getNombre());
	    }
	    if (empleadoActualizado.getApellido1() != null) {
	        empleadoExistente.setApellido1(empleadoActualizado.getApellido1());
	    }
	    if (empleadoActualizado.getApellido2() != null) {
	        empleadoExistente.setApellido2(empleadoActualizado.getApellido2());
	    }
	    if (empleadoActualizado.getDepartamento() != null) {
	        empleadoExistente.setDepartamento(empleadoActualizado.getDepartamento());
	    }
	    if (empleadoActualizado.getPerfil() != null) {
	        empleadoExistente.setPerfil(empleadoActualizado.getPerfil());
	    }
	    if (empleadoActualizado.getCentro() != null) {
	        empleadoExistente.setCentro(empleadoActualizado.getCentro());
	    }
	    if(empleadoActualizado.getFechaalta() != null) {
	    	empleadoExistente.setFechaalta(empleadoActualizado.getFechaalta());
	    }
	    if (empleadoActualizado.getClavesecreta() != null) {
	        empleadoExistente.setClavesecreta(empleadoActualizado.getClavesecreta());
	    }
	    if (empleadoActualizado.getTwoFA() != null) {
	        empleadoExistente.setTwoFA(empleadoActualizado.getTwoFA());
	    }

	    // Guarda los cambios
	    this.empdao.save(empleadoExistente);

	}


	///////////////////////////
	//COMPROBAR EMP BLOQ./VERIFICADO
	///////////////////////////
	public boolean comprobar(String email) {
		boolean res = false;
		Empleado e = this.empdao.findByEmail(email);
		if (Objects.isNull(e)) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No existe el empleado seleccionado");
		}
		if (e.isBloqueado() || !e.isVerificado()) {
			res = true;
		}
		
		return res;
	}

	///////////////////////////
	//BUSCAR ROL POR EMAIL
	///////////////////////////
	public String getUserRoleByEmail(String email) {
	    String rol = "";

	    // Intenta buscar al empleado por el email
	    Empleado empleado = empdao.findByEmail(email);
	    if (empleado != null) {
	        rol = "empleado";
	    } else {
	        // Intenta buscar al administrador por el email solo si no se encontró un empleado
	        Administrador admin = admindao.findByEmail(email);
	        if (admin != null) {
	            rol = "administrador";
	        }
	    }

	    // Si no se encontró ni como empleado ni como administrador, lanza una excepción
	    if (rol.isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un usuario con ese email");
	    }

	    return rol;
	}
	
	/////////////////////////////////////
	// COMPROBAR REUNIONES DE UN USUARIO
	/////////////////////////////////////
	public boolean comprobarReuniones(String email, LocalDateTime inicio, LocalDateTime fin) {
		
		List<Reunion> l1 = this.eserv.reunionesOrganizadas(email);
		List<Reunion> l2 = this.eserv.reunionesAsistidas(email);
		
		Set<Reunion> conjunto = new HashSet<>(l1);
        conjunto.addAll(l2);
        List<Reunion> reunionessinfiltrar = new ArrayList<>(conjunto);
        
        List<Reunion> reunionesFiltradas = reunionessinfiltrar.stream()
                .filter(reunion -> reunion.getInicio().isAfter(inicio) || reunion.getInicio().isEqual(inicio))
                .filter(reunion -> reunion.getFin().isBefore(fin) || reunion.getFin().isEqual(fin))
                .collect(Collectors.toList());
        
        if (reunionesFiltradas.isEmpty() || Objects.isNull(reunionesFiltradas)) {
        	return true;
        } else {
        	return false;
        }
	}

}
