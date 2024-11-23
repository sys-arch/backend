package com.equipo3.reuneme.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.dao.AsistenteDAO;
import com.equipo3.reuneme.dao.AusenciaDAO;
import com.equipo3.reuneme.dao.EmpleadoDAO;
import com.equipo3.reuneme.dao.ReunionDAO;
import com.equipo3.reuneme.dao.TurnoDAO;
import com.equipo3.reuneme.model.Asistente;
import com.equipo3.reuneme.model.AsistenteId;
import com.equipo3.reuneme.model.Ausencia;
import com.equipo3.reuneme.model.Empleado;
import com.equipo3.reuneme.model.Reunion;
import com.equipo3.reuneme.model.Turno;
import com.equipo3.reuneme.model.EstadoReunion;
import com.equipo3.reuneme.model.RegistroReunion;
import com.equipo3.reuneme.model.EstadoAsistente;

@Service
public class EmpleadoService {
	
	@Autowired
	private EmpleadoDAO edao;
	
    @Autowired
    private ReunionDAO reunionRepository;

    @Autowired
    private AsistenteDAO asistenteRepository;
    
    @Autowired
    private AusenciaDAO audao;
    
    @Autowired
    private TurnoDAO tdao;
	
	/////////////////////////
	//VER DATOS (DEL PROPIO EMPLEADO)
	/////////////////////////
	public Empleado verDatos(String email) {
	    Empleado empleado = this.edao.findByEmail(email);
	    if (Objects.isNull(empleado)) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado");
	    }
	    return empleado;
	}


	/////////////////////////
	//ACTUALIZAR CONTRASEÑA EMPLEADO
	/////////////////////////
	public void actualizarPwd(String email, String pwd) {
		Empleado e = this.edao.findByEmail(email);
		
		if(Objects.isNull(e)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El empleado no existe!");
		}
		
		this.edao.delete(e);
		pwd = org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
		e.setPwd(pwd);
		this.edao.save(e);
		
	}
	
	/////////////////////////
	//AÑADIR REUNIÓN
	/////////////////////////
	public Reunion anadirReunion(RegistroReunion reunion) {
		
		Empleado emp = this.edao.findByEmail(reunion.getOrganizador());
		
		if(Objects.isNull(emp)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El organizador proporcionado no existe o no es un empleado");
		}
		
		Reunion re = new Reunion();
		re.setEstado(reunion.getEstado());
		re.setFin(formatoFechaHora(reunion.getFin()));
		re.setInicio(formatoFechaHora(reunion.getInicio()));
		re.setOrganizador(emp);
		re.setObservaciones(reunion.getObservaciones());
		re.setUbicacion(reunion.getUbicacion());
		re.setAsunto(reunion.getAsunto());
		
		List<Turno> turnos = this.tdao.findAll();
		
		if(Objects.isNull(turnos)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El organizador proporcionado no existe o no es un empleado");
		}
		
		Boolean valido = false;
		
	    for (Turno turno : turnos) {
	        if (reunionTurno(re, turno)) {
	            valido = true;
	        }
	    }
	    
		if(!valido) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No se puede programar una reunión fuera de los turnos establecidos");
		}
        
        return reunionRepository.save(re);
    }
	
	//Conversion String a LocalDateTime
	private LocalDateTime formatoFechaHora(String fh) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return LocalDateTime.parse(fh, formatter);
	}
	
	public boolean reunionTurno(Reunion reunion, Turno turno) {
	    LocalTime inicioReunion = reunion.getInicio().toLocalTime();
	    LocalTime finReunion = reunion.getFin().toLocalTime();

	    LocalTime inicioTurno = turno.getHoraInicio().toLocalTime();
	    LocalTime finTurno = turno.getHoraFinal().toLocalTime();

	    return !inicioReunion.isBefore(inicioTurno) && !finReunion.isAfter(finTurno);
	}

	/////////////////////////
	//CANCELAR REUNIÓN
	/////////////////////////
    public Reunion cancelarReunion(Long id) {
        Reunion reunion = verReunion(id);
        reunion.setEstado(EstadoReunion.CANCELADA);
        return reunionRepository.save(reunion);
    }

	/////////////////////////
	//MODIFICAR REUNIÓN
	/////////////////////////
    public Reunion modificarReunion(Long id, Reunion reunionModificada) {
        Reunion reunion = verReunion(id);

        if (reunion.getEstado() == EstadoReunion.CERRADA || reunion.getEstado() == EstadoReunion.CANCELADA || reunion.getEstado() == EstadoReunion.REALIZADA) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No se puede modificar una reunión cerrada, cancelada o que ya ha sido realizada");
        }

        LocalDateTime ahora = LocalDateTime.now();
        if (ahora.isAfter(reunion.getInicio().minusDays(1).withHour(23).withMinute(59))) {
            if (reunion.getEstado() == EstadoReunion.ABIERTA) {
                reunion.setEstado(EstadoReunion.CERRADA);
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La reunión estaba abierta y ha sido cerrada automáticamente por exceder el límite de tiempo de modificación");
            }
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No se puede modificar la reunión después de las 23:59 del día anterior");
        }

        // Actualizar los campos permitidos
        reunion.setInicio(reunionModificada.getInicio());
        reunion.setFin(reunionModificada.getFin());
        reunion.setUbicacion(reunionModificada.getUbicacion());
        reunion.setObservaciones(reunionModificada.getObservaciones());
        return reunionRepository.save(reunion);
    }
    
	/////////////////////////
	//CERRAR REUNIÓN
	/////////////////////////
    public Reunion cerrarReunion(Long id) {
        Reunion reunion = verReunion(id);
        reunion.setEstado(EstadoReunion.CERRADA);
        return reunionRepository.save(reunion);
    }

	/////////////////////////
	//AÑADIR ASISTENTE
	/////////////////////////
    public void anadirAsistente(Long idReunion, String email) {
        
    	Empleado emp = this.edao.findByEmail(email);
    	
		if(Objects.isNull(emp)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El empleado no existe!");
		}
    		
    	AsistenteId asistenteId = new AsistenteId(idReunion, emp.getId());
        if (asistenteRepository.existsById(asistenteId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El asistente ya está registrado en esta reunión");
        }
        
        List<Ausencia> ausencias = this.audao.findAllByEmpleado(emp);
		Reunion reunion = this.reunionRepository.getReferenceById(idReunion);
		
		if(Objects.isNull(reunion)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La reunion no existe!");
		}
		
		if(reunion.getOrganizador().getEmail().equals(email)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario al que intenta añadir es organizador");
		}

		if (!Objects.isNull(ausencias)) {
		    // Filtrar ausencias eliminando aquellas que finalizan antes de hoy
		    LocalDateTime now = LocalDateTime.now();
		    List<Ausencia> ausenciasFiltradas = ausencias.stream()
		            .filter(ausencia -> ausencia.getFechaFin().isAfter(now))
		            .collect(Collectors.toList());

		    // Procesar las ausencias filtradas
		    for (Ausencia ausencia : ausenciasFiltradas) {
		        LocalDateTime ausenciaInicio = ausencia.getFechaInicio();
		        LocalDateTime ausenciaFin = ausencia.getFechaFin();

		        // Verificar si hay superposición entre la reunión y la ausencia
		        if (reunion.getInicio().isBefore(ausenciaFin) && ausenciaInicio.isBefore(reunion.getFin())) {
		            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario que intenta añadir estará ausente en las fechas de la reunión");
		        }
		    }
		}
        
        Asistente asistente = new Asistente();
        asistente.setIdReunion(idReunion);
        asistente.setIdUsuario(emp.getId());
        asistente.setEstado(EstadoAsistente.PENDIENTE);
        asistente.setAsiste(false);
        asistenteRepository.save(asistente);
    }

	/////////////////////////
	//ELIMINAR ASISTENTE
	/////////////////////////
    public void eliminarAsistente(Long idReunion, String idUsuario) {
        AsistenteId asistenteId = new AsistenteId(idReunion, idUsuario);
        if (!asistenteRepository.existsById(asistenteId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El asistente no existe en esta reunión");
        }
        asistenteRepository.deleteById(asistenteId);
    }

	
	/////////////////////////
	//ACTUALIZAR ESTADO DE ASISTENCIA
	/////////////////////////
	public Asistente actualizarEstadoAsistencia(Long idReunion, String idUsuario, EstadoAsistente estado) {
	Asistente asistente = obtenerAsistente(idReunion, idUsuario);
	
	// Verifica que el estado sea válido
	if (estado != EstadoAsistente.ACEPTADA && estado != EstadoAsistente.RECHAZADA) {
	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado de asistencia no válido");
	}
	
	asistente.setEstado(estado);
	return asistenteRepository.save(asistente);
	}



	/////////////////////////
	//ASISTIR (COMO ASISTENTE A REUNION)
	/////////////////////////
    public Asistente asistir(Long idReunion, String idUsuario) {
        Asistente asistente = obtenerAsistente(idReunion, idUsuario);
        Reunion reunion = verReunion(idReunion);

        LocalDateTime ahora = LocalDateTime.now();
        if (ahora.isBefore(reunion.getInicio()) || ahora.isAfter(reunion.getFin())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo se puede registrar asistencia dentro de las horas de la reunión");
        }

        asistente.setAsiste(true);
        return asistenteRepository.save(asistente);
    }
    
	/////////////////////////
	//VER REUNIÓN
	/////////////////////////
	public Reunion verReunion(Long id) {
        return reunionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reunión no encontrada"));
	}

	/////////////////////////
	//COMPROBAR SI USUARIO ASISTE A TAL REUNION
	/////////////////////////
	public Asistente obtenerAsistente(Long idReunion, String idUsuario) {
	AsistenteId asistenteId = new AsistenteId(idReunion, idUsuario);
	return asistenteRepository.findById(asistenteId)
	.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asistente no encontrado"));
	}

    
	/////////////////////////
	//OBTENER ASISTENTES DE REUNION
	/////////////////////////
    public List<Asistente> obtenerAsistentesPorReunion(Long idReunion) {
        // Verifica si la reunión existe
        if (!reunionRepository.existsById(idReunion)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reunión no encontrada");
        }
        // Retorna la lista de asistentes a la reunión
        return asistenteRepository.findByIdReunion(idReunion);
    }


	/////////////////////////
	//LISTADO EMPLEADOS A REUNIÓN
	/////////////////////////
	public List<Empleado> posiblesAsistentes() {
		List<Empleado> lista = this.edao.findAll();

		if (lista.isEmpty() || Objects.isNull(lista)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No existen empleados para añadir como asistente");
		}
		
		lista.removeIf(empleado -> empleado.isBloqueado() && !empleado.isVerificado());

		return lista;
	}
	
	/////////////////////////
	//LISTADO REUNIONES
	/////////////////////////
	public List<Reunion> listadoReuniones() {
		List<Reunion> lista = this.reunionRepository.findAll();

		if (lista.isEmpty() || Objects.isNull(lista)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No existen reuniones");
		}

		return lista;
	}

	///////////////////////////////////
	// CONSEGUIR REUNIONES ORGANIZADAS
	//////////////////////////////////
	public List<Reunion> reunionesOrganizadas(String email) {
    	Empleado emp = this.edao.findByEmail(email);
    	
		if(Objects.isNull(emp)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El empleado no existe!");
		}
		
		List<Reunion> reuniones = this.reunionRepository.findByOrganizador(emp);
		
		if (reuniones.isEmpty() || Objects.isNull(reuniones)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No existen reuniones");
		}
		
		return reuniones;
	}

	////////////////////////////////////
	// OBTENER REUNIONES QUE ASISTE
	////////////////////////////////////
	public List<Reunion> reunionesAsistidas(String email) {
    	Empleado emp = this.edao.findByEmail(email);
    	
		if(Objects.isNull(emp)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El empleado no existe!");
		}

		List<Long> ids = this.asistenteRepository.findReunionIdsByIdUsuario(emp.getId());
		System.out.println(ids);
		if (ids.isEmpty() || Objects.isNull(ids)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El empleado no tiene reuniones asociadas como asistente");
		}
		
		return this.reunionRepository.findAllById(ids);
	}
	////////////////////////////////////
	//OBTENER REUNIONES ASISTIDAS PENDIENTES
	////////////////////////////////////
	public List<Reunion> reunionesAsistidasPendientes(String email) {
	Empleado emp = this.edao.findByEmail(email);
	
	if (Objects.isNull(emp)) {
	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El empleado no existe!");
	}
	
	// Obtener IDs de reuniones donde el usuario es asistente
	List<Long> ids = this.asistenteRepository.findReunionIdsByIdUsuarioAndEstado(emp.getId(), EstadoAsistente.PENDIENTE);
	
	if (ids.isEmpty() || Objects.isNull(ids)) {
	throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El empleado no tiene reuniones pendientes asociadas como asistente");
	}
	
	// Retornar reuniones con estado pendiente
	return this.reunionRepository.findAllById(ids);
	}


}
