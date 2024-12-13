package com.equipo3.reuneme.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.access.prepost.PreAuthorize;

import com.equipo3.reuneme.model.Administrador;
import com.equipo3.reuneme.model.Ausencia;
import com.equipo3.reuneme.model.Empleado;
import com.equipo3.reuneme.model.RegistroAdmin;
import com.equipo3.reuneme.model.RegistroAusencia;
import com.equipo3.reuneme.model.Turno;
import com.equipo3.reuneme.model.Usuario;
import com.equipo3.reuneme.service.AdminService;
import com.equipo3.reuneme.service.EmailService;
import com.equipo3.reuneme.service.PasswordService;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    EmailService emailservice;
    
    @Autowired
    PasswordService pwdservice;

    @Autowired
    AdminService adminservice;

    /*********************************
     *REGISTRO DE ADMINISTRADOR
     ********************************/
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public void registerAdmin(@RequestBody RegistroAdmin re) {
        // Comprobamos que ambas contraseñas son iguales
    	if(!this.pwdservice.isSamePwd(re.getPwd1(), re.getPwd2())) {
    		throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Las contraseñas no son iguales");
    	}
    	
    	// Comprobamos que la contraseña cumple requisitos de seguridad
    	if(!this.pwdservice.isValid(re.getPwd1())) {
    		throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, 
    				"Las contraseña no cumple con los requisitos de seguridad: "
    				+ "Entre 8 y 24 caracteres, Debe contener una maysucula, una minuscula, un digito, "
    				+ "un caracter especial y no debe contener espacios");
    	}
        

        // Comprobamos que el email tiene un formato válido
        if (!emailservice.validarEmail(re.getEmail().toLowerCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email insertado no tiene un formato válido: "
            		+ "usuario@dominio.com");
        }

        // Si pasa los controles, se registra en BD
        Administrador admin = new Administrador();
        admin.setEmail(re.getEmail().toLowerCase());
        admin.setPwd(re.getPwd1());
        admin.setNombre(re.getNombre());
        admin.setApellido1(re.getApellido1());
        admin.setApellido2(re.getApellido2());
        admin.setCentro(re.getCentro());
        admin.setInterno(re.isInterno());
        
        this.adminservice.registrarAdmin(admin);
    }
    
    /*********************************
     *MODIFICAR EMPLEADO
     ********************************/
    
    @PutMapping("/modificarEmpleado")
    public void modificarEmpleado(@RequestBody Empleado emp) {
    	try {
    		adminservice.actualizarEmpleado(emp.getEmail().toLowerCase(), emp);
    	} catch (Exception e) {
    		throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Error al modificar el empleado.");
    	}
	}
    
    /*********************************
     *VERIFICAR EMPLEADO
     ********************************/    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/verificarEmpleado")
    public void verificarEmpleado(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        email = email.toLowerCase();
        System.out.println(email);

        if (!this.emailservice.validarEmail(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El email no tiene un buen formato");
        }
        
        this.adminservice.verificarEmpleado(email);
    }

    /*********************************
     *BLOQUEAR/DESBLOQUEAR EMPLEADO
     ********************************/
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/cambiarEstadoBloqueo")
    public void cambiarEstadoBloqueoEmpleado(@RequestParam String email, @RequestParam boolean bloquear) {
    	
    	email = email.toLowerCase();
    	
        if (!this.emailservice.validarEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email no tiene un formato válido: usuario@dominio.com");
        }
        this.adminservice.cambiarEstadoBloqueoEmpleado(email, bloquear);
    }

	    
    /*********************************
     *BORRAR EMPLEADO
     ********************************/
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/borrarEmpleado")
    public void borrarUsuario(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        email = email.toLowerCase();
        
        if (email == null || email.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email es obligatorio");
        }
        
        this.adminservice.deleteUser(email);
    }
  
    /*********************************
     *OBTENER LISTA DE EMPLEADOS
     ********************************/
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listaEmpleados")
    public List<Empleado> verEmpleados () {
    	return this.adminservice.getEmpleados();
    }
    
    /*********************************
     *OBTENER EMPLEADO
     ********************************/
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/obtenerEmpleado")
    public Empleado verEmpleado (@RequestParam String email) {
    	
    	email = email.toLowerCase();
    	
    	if (!this.emailservice.validarEmail(email)) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El email no tiene un formato adecuado");
    	}
    	
    	return this.adminservice.getEmpleado(email);
    }
    
    /*********************************
     *CONSULTAR AUSENCIAS DE UN EMPLEADO
     ********************************/
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/todasAusencias")
	public List<Ausencia> getAusencias() {
	    return this.adminservice.consultarAusencias(); 
	}
	
	/*********************************
     *CONSULTAR TODAS LAS AUSENCIAS
     ********************************/
	@GetMapping("/todasAusencias")
    public List<Ausencia> obtenerTodasLasAusencias() {
        return adminservice.obtenerTodasLasAusencias();
    }
	
	/*********************************
     *ELIMINAR AUSENCIA
     ********************************/

	@DeleteMapping("/eliminarAusencia/{id}")
    public void eliminarAusencia(@PathVariable String id) {
        adminservice.eliminarAusencia(id);
    }

    /*********************************
     *AÑADIR AUSENCIA
     ********************************/
    @PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/anadirAusencia")
	public Ausencia anadirAusencia(@RequestParam String email, @RequestBody RegistroAusencia ausencia) {
		
    	if (!this.emailservice.validarEmail(email)) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Revise el email, no es correcto");
    	}
    	
    	if(Objects.isNull(ausencia)) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La ausencia que se intenta registrar está vacía");
    	}
		
	    return adminservice.anadirAusencias(email, ausencia); 
	}
    /*********************************
     *VER LISTA DE USUARIOS
     ********************************/
    @PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/all")
	@ResponseStatus(HttpStatus.OK)
	public List<Usuario> verTodosLosUsuarios() {
	    return this.adminservice.obtenerTodosLosUsuarios();
	}
	
	/*********************************
	 * VER DATOS DEL ADMINISTRADOR
	 ********************************/
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/verDatos")
	public Administrador verDatosAdmin(@RequestParam String email) {
	    if (!this.emailservice.validarEmail(email)) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email no tiene un formato válido: usuario@dominio.com");
	    }
	    return this.adminservice.verDatos(email);
	}
	
    /*********************************
     *AÑADIR TURNO
     ********************************/
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/anadirTurnos")
	@ResponseStatus(HttpStatus.OK)
	public void anadirTurnos( @RequestBody List<Turno> turnos) {
    	if (turnos.isEmpty() || Objects.isNull(turnos)) {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No hay turnos para añadir");
    	}
    	
    	this.adminservice.anadirTurnos(turnos);
	}
	
    /*********************************
     *DEVOLVER TODOS LOS TURNOS
     ********************************/
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/turnos")
	@ResponseStatus(HttpStatus.OK)
	public List<Turno> turnos() {    	
    	return this.adminservice.turnos();
	}
	
	/*********************************
     *MODIFICAR ADMINISTRADOR
     ********************************/
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/modificarAdministrador")
    public void modificarAdministrador(@RequestBody Administrador admin) {
    	try {
    		adminservice.actualizarAdministrador(admin.getEmail(), admin);  
    	} catch (Exception e) {
    		throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Error al modifficar el empleado.");
    	}
	}
    
	/*********************************
     *COMPROBAR BLOQ./VALIDADO EMPLEADO
     ********************************/
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/verificar")
    public boolean verificar(@RequestBody String email) {
    	
    	email = email.toLowerCase();
    	
	    if (!this.emailservice.validarEmail(email)) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email no tiene un formato válido: usuario@dominio.com");
	    }
	    return this.adminservice.comprobar(email);
	}

    /*********************************
     * OBTENER ROL DE USUARIO POR EMAIL
     ********************************/
    @GetMapping("/getUserRoleByEmail")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> getUserRoleByEmail(@RequestParam String email) {
        try {
            String role = adminservice.getUserRoleByEmail(email);
            Map<String, String> response = new HashMap<>();
            response.put("role", role); // Clave "role" con el valor del rol
            return response;
        } catch (ResponseStatusException e) {
            throw e; // Lanza la excepción tal cual para que el controlador maneje la respuesta HTTP
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener el rol del usuario.");
        }
    }
    
    /*********************************
     * COMPROBAR REUNIONES DE EMPLEADO
     ********************************/
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/comprobarReuniones")
    @ResponseStatus(HttpStatus.OK)
    public boolean comprobarReuniones(@RequestBody Map<String, String> payload) {
    	
    	String email = payload.get("email");
    	LocalDateTime inicio = LocalDateTime.parse(payload.get("inicio"));
    	LocalDateTime fin = LocalDateTime.parse(payload.get("fin"));
    	email = email.toLowerCase();
    	
	    if (!this.emailservice.validarEmail(email)) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email no tiene un formato válido: usuario@dominio.com");
	    }
	    
	    return this.adminservice.comprobarReuniones(email, inicio, fin);
    }


}
        
        
