package com.equipo3.reuneme.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ausencias")
public class Ausencia {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;  

    @Column(nullable = false)
    private String motivo;
    
    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    private Empleado empleado;

    public Ausencia(LocalDateTime fechaInicio, LocalDateTime fechaFin, String motivo, Empleado empleado ) {
        // Validamos las fechas al crear la instancia
        validarFechas(fechaInicio, fechaFin);
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.motivo = motivo;
        this.empleado = empleado;
    }
    
    public Ausencia() {}

    // Getters y setters
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        // Validamos la fecha al modificarla
        validarFechas(fechaInicio, fechaFin);
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
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
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Empleado getEmpleado() {
		return this.empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

    // Método privado para validar las fechas
    private void validarFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
    	LocalDateTime fechaActual = LocalDateTime.now();
        if (fechaInicio.isBefore(fechaActual)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser anterior a la fecha actual.");
        }
        if (fechaFin.isBefore(fechaInicio) || fechaFin.isBefore(fechaActual)) {
            throw new IllegalArgumentException("La fecha de fin debe ser igual o posterior a la fecha de inicio y a la fecha actual.");
        }
    }
}
