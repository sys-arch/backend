package com.equipo3.reuneme.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "ausencias")
public class Ausencia {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;

    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;  

    @Column(nullable = false)
    private String motivo;
    
    @ManyToOne
    @JoinColumn(name = "fk_usuario", nullable = false)
    private Usuario usuario;

    public Ausencia(Date fechaInicio, Date fechaFin, String motivo, Usuario usuario ) {
        // Validamos las fechas al crear la instancia
        validarFechas(fechaInicio, fechaFin);
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.motivo = motivo;
        this.usuario = usuario;
    }
    
    public Ausencia() {}

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
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
