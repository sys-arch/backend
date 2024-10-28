package com.equipo3.reuneme.service;


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
	    System.out.println("Token generado y almacenado: " + token.getId());
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
	public Token obtenerToken(String idToken) {
	    return this.tokens.get(idToken);
	}

	
	public boolean isTokenValid(String idToken) {
	    Token token = this.tokens.get(idToken);
	    if (token == null) {
	        System.out.println("Token no encontrado: " + idToken);
	        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token no válido o inexistente.");
	    }
	    if (token.caducado()) {
	        this.tokens.remove(idToken);
	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "El token ha expirado.");
	    }
	    System.out.println("Token encontrado y válido: " + idToken);
	    return true;
	}





	
	public void eliminarToken(String idToken) {
		this.tokens.remove(idToken);
	}
	
	public String obtenerEmail(String idToken) {
        Token token = this.tokens.get(idToken);
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token no encontrado");
        }
        return token.getEmail();
    }

}
