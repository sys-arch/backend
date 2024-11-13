package com.equipo3.reuneme.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

@RestController
@RequestMapping(value = "/empleados")
@CrossOrigin(origins = "*", methods= {RequestMethod.PUT,RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE})
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
	// CONFIRMAR ASISTENCIA
	////////////////////////////////////
    @PutMapping("/reunion/{idReunion}/asistente/{idUsuario}/confirmar")
    public Asistente confirmarAsistencia(@PathVariable Long idReunion, @PathVariable String idUsuario) {
        return empleadoService.confirmarAsistencia(idReunion, idUsuario);
    }

	////////////////////////////////////
	//ASISTIR
	////////////////////////////////////
    @PutMapping("/reunion/{idReunion}/asistente/{idUsuario}/asistir")
    public Asistente asistir(@PathVariable Long idReunion, @PathVariable String idUsuario) {
        return empleadoService.asistir(idReunion, idUsuario);
    }
    
    /*********************************
     *OBTENER LISTA DE EMPLEADOS
     ********************************/
    @GetMapping("/reunion/asistentes")
    public List<Empleado> posiblesAsistentes () {
    	return this.empleadoService.posiblesAsistentes(null);
    }
	
}
