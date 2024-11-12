package com.equipo3.reuneme.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.dao.AsistenteDAO;
import com.equipo3.reuneme.dao.AusenciaDAO;
import com.equipo3.reuneme.dao.EmpleadoDAO;
import com.equipo3.reuneme.dao.ReunionDAO;
import com.equipo3.reuneme.model.Asistente;
import com.equipo3.reuneme.model.AsistenteId;
import com.equipo3.reuneme.model.Ausencia;
import com.equipo3.reuneme.model.Empleado;
import com.equipo3.reuneme.model.Reunion;
import com.equipo3.reuneme.model.EstadoReunion;
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
	
	/////////////////////////
	//VER DATOS (DEL PROPIO EMPLEADO)
	/////////////////////////
	public Empleado verDatos(String email) {
	    Empleado empleado = this.edao.findByEmail(email);
	    if (empleado == null) {
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
	public Reunion añadirReunion(Reunion reunion) {
        if (reunionRepository.existsById(reunion.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La reunión ya existe");
        }
        
        return reunionRepository.save(reunion);
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

        if (reunion.getEstado() == EstadoReunion.CERRADA || reunion.getEstado() == EstadoReunion.CANCELADA) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No se puede modificar una reunión cerrada o cancelada");
        }

        LocalDateTime ahora = LocalDateTime.now();
        if (ahora.isAfter(reunion.getInicio().minusDays(1).withHour(23).withMinute(59))) {
            if (reunion.getEstado() == EstadoReunion.ABIERTA) {
                reunion.setEstado(EstadoReunion.CERRADA);
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La reunión está abierta y ha sido cerrada automáticamente por exceder el límite de tiempo de modificación");
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

//	/////////////////////////
//	//AÑADIR ASISTENTE
//	/////////////////////////
//    public Asistente añadirAsistente(Long idReunion, String idUsuario) {
//        AsistenteId asistenteId = new AsistenteId(idReunion, idUsuario);
//        if (asistenteRepository.existsById(asistenteId)) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "El asistente ya está registrado en esta reunión");
//        }
//        
//        List<Ausencia> ausencias = this.audao.findByIdUsuario(idUsuario);
//		Reunion reunion = this.reunionRepository.getReferenceById(idReunion);
//        
//        for (Ausencia ausencia : ausencias) {
//            // Convertir las fechas de ausencia de Date a LocalDateTime
//            LocalDateTime ausenciaInicio = ausencia.getFechaInicio().toInstant()
//                    .atZone(ZoneId.systemDefault()).toLocalDateTime();
//            LocalDateTime ausenciaFin = ausencia.getFechaFin().toInstant()
//                    .atZone(ZoneId.systemDefault()).toLocalDateTime();
//
//            // Verificar si hay superposición entre la reunión y la ausencia
//            // Si hay superposición, el usuario no está disponible
//            if (reunion.getInicio().isBefore(ausenciaFin)
//                    || ausenciaInicio.isBefore(reunion.getFin())) {
//            	throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario que intenta añadir estará ausente en las fechas de la reunión");
//            }
//        }
//        
//        Asistente asistente = new Asistente();
//        asistente.setIdReunion(idReunion);
//        asistente.setIdUsuario(idUsuario);
//        asistente.setEstado(EstadoAsistente.PENDIENTE);
//        asistente.setAsiste(false);
//        return asistenteRepository.save(asistente);
//    }

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
	//CONFIRMAR ASISTENCIA
	/////////////////////////
    public Asistente confirmarAsistencia(Long idReunion, String idUsuario) {
        Asistente asistente = obtenerAsistente(idReunion, idUsuario);
        asistente.setEstado(EstadoAsistente.ACEPTADA);
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
    private Asistente obtenerAsistente(Long idReunion, String idUsuario) {
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
	public List<Empleado> posiblesAsistentes(Object object) {
		List<Empleado> lista = this.edao.findAll();

		if (lista.isEmpty() || Objects.isNull(lista)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No existen empleados para añadir como asistente");
		}
		
		lista.removeIf(empleado -> empleado.isBloqueado() && !empleado.isVerificado());

		return lista;
	}

}
