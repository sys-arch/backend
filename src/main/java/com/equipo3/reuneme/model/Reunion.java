/*package com.equipo3.reuneme.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reuniones")
public class Reunion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "asunto", nullable = false)
	private String asunto;
	
	@Column(name = "ubicacion", nullable = false)
	private String ubicacion;
	
	@Column(name="observaciones")
	private String observaciones;
	
	

	    @Column(name = "fecha_fin")
	    @Temporal(TemporalType.DATE)
	    private Date fechaFin;  

	    @Column(nullable = false)
	    private String motivo;
	    
	    @ManyToOne
	    @JoinColumn(name = "fk_usuario", nullable = false)
	    private Usuario usuario;

}*/
