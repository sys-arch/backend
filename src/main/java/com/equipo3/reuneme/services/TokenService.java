package com.equipo3.reuneme.services;


import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.model.Token;

@Service
public class TokenService {
	
private Map<String,Token>tokens = new HashMap<>();
	
	public String generarToken(Token token) {
		this.tokens.put(token.getId(), token);
		return token.getId();
	}
	
	public void validarToken(String idToken) {
		Token token = this.tokens.get(idToken);
		if(token ==null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Usted se está intentando colar en el sistema");
		}
		if(token.caducado()) {
			this.tokens.remove(idToken);
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"El token ha expirado");
		}
		token.incrementarTiempo();
	}
	
	public void eliminarToken(String idToken) {
		this.tokens.remove(idToken);
	}

}
