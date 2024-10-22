package com.equipo3.reuneme.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.dao.AusenciaDAO;
import com.equipo3.reuneme.model.Ausencia;

@Service
public class AusenciaService {
	
	@Autowired
	private AusenciaDAO audao;
	
	public void insertar(Ausencia a) {
		this.audao.save(a);
	}
	
	public List<Ausencia> getAusencias (String id) {
		
		List<String> miLista = new ArrayList<>();
		miLista.add(id);
		List<Ausencia> lista = this.audao.findAllById(miLista);
		
		if (Objects.isNull(lista) || lista.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No hay ausencias");
		}
		
		return lista;
	}

}
