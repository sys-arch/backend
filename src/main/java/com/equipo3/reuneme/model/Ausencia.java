package com.equipo3.reuneme.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "Ausencia", indexes = {
		@Index(columnList = "idUsuario")
	})
public class Ausencia {
	
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	
    @Column(nullable = false)
    private Date fechaInicio;
    
    @Column(nullable = false)
    private Date fechaFin;
    
    @Column(length = 254, nullable = false)
    private String motivo;
    
    @Column(length = 36, nullable = false)
    private String idUsuario;

    public Ausencia(Date fechaInicio, Date fechaFin, String motivo, String idUsuario) {
        // Validamos las fechas al crear la instancia
        validarFechas(fechaInicio, fechaFin);
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.motivo = motivo;
        this.idUsuario = idUsuario;
    }

    // Getters y setters
    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        // Validamos la fecha al modificarla
        validarFechas(fechaInicio, fechaFin);
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        // Validamos la fecha al modificarla
        validarFechas(fechaInicio, fechaFin);
        this.fechaFin = fechaFin;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    // Método privado para validar las fechas
    private void validarFechas(Date fechaInicio, Date fechaFin) {
        Date fechaActual = new Date();
        if (fechaInicio.before(fechaActual)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser anterior a la fecha actual.");
        }
        if (fechaFin.before(fechaInicio) || fechaFin.before(fechaActual)) {
            throw new IllegalArgumentException("La fecha de fin debe ser igual o posterior a la fecha de inicio y a la fecha actual.");
        }
    }
}
