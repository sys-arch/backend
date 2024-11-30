package com.equipo3.reuneme.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.equipo3.reuneme.model.Asistente;
import com.equipo3.reuneme.model.AsistenteId;
import com.equipo3.reuneme.model.EstadoAsistente;


public interface AsistenteDAO extends JpaRepository<Asistente, AsistenteId> {
	
	List<Asistente> findByIdReunion(Long idReunion);

    @Query("SELECT a.idReunion FROM Asistente a WHERE a.idUsuario = :idUsuario")
    List<Long> findReunionIdsByIdUsuario(@Param("idUsuario") String idUsuario);

    @Query("SELECT a.idReunion FROM Asistente a WHERE a.idUsuario = :idUsuario AND a.estado = :estado")
    List<Long> findReunionIdsByIdUsuarioAndEstado(@Param("idUsuario") String idUsuario, @Param("estado") EstadoAsistente estado);
    
}
