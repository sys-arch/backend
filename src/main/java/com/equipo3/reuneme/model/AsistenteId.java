package com.equipo3.reuneme.model;

import java.io.Serializable;
import java.util.Objects;

public class AsistenteId implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private Long idReunion;
    private String idUsuario;

    public AsistenteId() {}

    public AsistenteId(Long idReunion, String idUsuario) {
        this.idReunion = idReunion;
        this.idUsuario = idUsuario;
    }

    // Equals y HashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AsistenteId that = (AsistenteId) o;
        return Objects.equals(idReunion, that.idReunion) && Objects.equals(idUsuario, that.idUsuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReunion, idUsuario);
    }
}

