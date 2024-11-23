package com.equipo3.reuneme.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestParam;

import com.equipo3.reuneme.model.Asistente;
import com.equipo3.reuneme.model.Empleado;
import com.equipo3.reuneme.model.RegistroReunion;
import com.equipo3.reuneme.model.Reunion;
import com.equipo3.reuneme.service.EmailService;
import com.equipo3.reuneme.service.EmpleadoService;
import com.equipo3.reuneme.model.EstadoAsistente;
import com.equipo3.reuneme.model.AsistenteId;




@RestController
@RequestMapping(value = "/empleados")
public class EmpleadoController {

	@Autowired
	private EmpleadoService empleadoService;
	
	@Autowired
	private EmailService emailService;

	////////////////////////////////////
	// DEVOLVER INFORMACIÓN EMPLEADO
	////////////////////////////////////
	@GetMapping(value = "/verDatos")
	@ResponseStatus(HttpStatus.OK)
	public Empleado verDatos(@RequestParam String email) {
		
		email = email.toLowerCase();
		
	    if (!emailService.validarEmail(email)) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email insertado no tiene un formato válido: usuario@dominio.com");
	    }
	    
	    return this.empleadoService.verDatos(email);
	}

	
	////////////////////////////////////
	// ACTUALIZAR CONTRASEÑA
	////////////////////////////////////
	@PutMapping(value = "/actualizarPwd")
	@ResponseStatus(HttpStatus.OK)
	public void actualizarPwd(@RequestBody Map<String,String> info ) {
		
		String email = info.get("email").toString();
		String pwd = info.get("pwd").toString();
		
		email = email.toLowerCase();
		
        if (!emailService.validarEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email insertado no tiene un formato válido: "
            		+ "usuario@dominio.com");
        }
		
		this.empleadoService.actualizarPwd(email, pwd);
	}
	
	////////////////////////////////////
	// AÑADIR REUNION
	////////////////////////////////////
    @PostMapping("/reunion")
    public Reunion añadirReunion(@RequestBody RegistroReunion reunion) {
        return empleadoService.anadirReunion(reunion);
    }
    
	////////////////////////////////////
	// VER REUNION
	////////////////////////////////////
    @PostMapping("/reunion/{id}/ver")
    public Reunion verReunion(@PathVariable Long id) {
        return empleadoService.verReunion(id);
    }

	////////////////////////////////////
	// CANCELAR REUNIÓN
	////////////////////////////////////
    @PutMapping("/reunion/{id}/cancelar")
    public Reunion cancelarReunion(@PathVariable Long id) {
        return empleadoService.cancelarReunion(id);
    }

	////////////////////////////////////
	// MODIFICAR REUNIÓN
	////////////////////////////////////
    @PutMapping("/reunion/{id}/modificar")
    public Reunion modificarReunion(@PathVariable Long id, @RequestBody Reunion reunion) {
        return empleadoService.modificarReunion(id, reunion);
    }

	////////////////////////////////////
	// CERRAR REUNION
	////////////////////////////////////
    @PutMapping("/reunion/{id}/cerrar")
    public Reunion cerrarReunion(@PathVariable Long id) {
        return empleadoService.cerrarReunion(id);
    }

	////////////////////////////////////
	//AÑADIR ASISTENTE
	////////////////////////////////////
	@PostMapping("/reunion/{idReunion}/asistente/{email}")
	public void anadirAsistente(@PathVariable Long idReunion, @PathVariable String email) {
		
		email = URLDecoder.decode(email, StandardCharsets.UTF_8);
		
        if (!emailService.validarEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email insertado no tiene un formato válido: "
            		+ "usuario@dominio.com");
        }
		
		this.empleadoService.anadirAsistente(idReunion, email);
	}


	////////////////////////////////////
	// BORRAR ASISTENTE
	////////////////////////////////////
    @DeleteMapping("/reunion/{idReunion}/asistente/{idUsuario}")
    public void eliminarAsistente(@PathVariable Long idReunion, @PathVariable String idUsuario) {
        empleadoService.eliminarAsistente(idReunion, idUsuario);
    }

	////////////////////////////////////
	//ACTUALIZAR ESTADO DE ASISTENCIA
	////////////////////////////////////
	@PutMapping("/reunion/{idReunion}/asistente/{idUsuario}/estado")
	public Asistente actualizarEstadoAsistencia(
	@PathVariable Long idReunion,
	@PathVariable String idUsuario,
	@RequestParam EstadoAsistente estado) {
	return empleadoService.actualizarEstadoAsistencia(idReunion, idUsuario, estado);
	}


	////////////////////////////////////
	//ASISTIR O RECHAZAR
	////////////////////////////////////
    @PutMapping("/reunion/{idReunion}/asistente/{idUsuario}/asistir")
    public Asistente asistir(@PathVariable Long idReunion, @PathVariable String idUsuario) {
        return empleadoService.asistir(idReunion, idUsuario);
    }
    
	////////////////////////////////////
	// OBTENER LISTA DE POSIBLES ASISTENTES
	////////////////////////////////////
    @GetMapping("/reunion/asistentes")
    public List<Empleado> posiblesAsistentes () {
    	return this.empleadoService.posiblesAsistentes();
    }
    
	////////////////////////////////////
	// OBTENER LISTA DE REUNIONES
	////////////////////////////////////
    @GetMapping("/reunion/listado")
    public List<Reunion> listadoReuniones() {
    	return this.empleadoService.listadoReuniones();
    }
    
	////////////////////////////////////
	// OBTENER REUNIONES QUE ORGANIZA
	////////////////////////////////////
    @PutMapping("/reunion/organizador")
    public List<Reunion> reunionesOrganizadas(@RequestBody Map<String, String> payload) {
        // Extraer el email del JSON
        String email = payload.get("email");

        // Validar que el email no sea nulo o vacío
        if (email == null || email.trim().isEmpty() || !emailService.validarEmail(email.trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "El email insertado no tiene un formato válido: usuario@dominio.com");
        }

        // Llama al servicio con el email válido
        return this.empleadoService.reunionesOrganizadas(email.trim());
    }

    
	////////////////////////////////////
	// OBTENER REUNIONES QUE ASISTE
	////////////////////////////////////
    @PutMapping("/reunion/asiste")
    public List<Reunion> reunionesAsistidas(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        System.out.println("Email extraído del JSON: " + email);
        return this.empleadoService.reunionesAsistidas(email);
    }
    
	////////////////////////////////////
	// OBTENER INVITACIONES A REUNION
    ////////////////////////////////////
    @PutMapping("/reunion/asiste-pendiente")
    public List<Reunion> reunionesAsistidasPendientes(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");

        if (email == null || email.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email no puede estar vacío.");
        }

        return empleadoService.reunionesAsistidasPendientes(email.trim());
    }
	/////////////////////////
	//OBTENER ASISTENTE POR CORREO
	/////////////////////////
	@GetMapping("/reunion/{idReunion}/asistente")
	public Asistente obtenerAsistentePorEmail(@PathVariable Long idReunion, @RequestParam String email) {
	Empleado empleado = empleadoService.verDatos(email); // Busca al empleado usando su correo
	if (empleado == null) {
	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
	}
	
	// Busca al asistente utilizando el idReunion y el id del empleado
	return empleadoService.obtenerAsistente(idReunion, empleado.getId());
	}



}
