package com.equipo3.reuneme.model;

public class RegistroReunion {
	
	    private Long id;
	    private String organizador;
	    private String inicio;
	    private String fin;
	    private String ubicacion;
	    private String observaciones;
	    private EstadoReunion estado;
	    private String asunto;
	
	    public RegistroReunion() {
		}

		public RegistroReunion(Long id, String organizador, String inicio, String fin, String ubicacion,
				String observaciones, EstadoReunion estado, String asunto) {
			this.id = id;
			this.organizador = organizador;
			this.inicio = inicio;
			this.fin = fin;
			this.ubicacion = ubicacion;
			this.observaciones = observaciones;
			this.estado = estado;
			this.asunto = asunto;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getOrganizador() {
			return organizador;
		}

		public void setOrganizador(String organizador) {
			this.organizador = organizador;
		}

		public String getInicio() {
			return inicio;
		}

		public void setInicio(String inicio) {
			this.inicio = inicio;
		}

		public String getFin() {
			return fin;
		}

		public void setFin(String fin) {
			this.fin = fin;
		}

		public String getUbicacion() {
			return ubicacion;
		}

		public void setUbicacion(String ubicacion) {
			this.ubicacion = ubicacion;
		}

		public String getObservaciones() {
			return observaciones;
		}

		public void setObservaciones(String observaciones) {
			this.observaciones = observaciones;
		}

		public EstadoReunion getEstado() {
			return estado;
		}

		public void setEstado(EstadoReunion estado) {
			this.estado = estado;
		}

		public String getAsunto() {
			return asunto;
		}

		public void setAsunto(String asunto) {
			this.asunto = asunto;
		}

}
