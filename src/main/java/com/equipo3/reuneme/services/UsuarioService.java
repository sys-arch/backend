package com.equipo3.reuneme.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.equipo3.reuneme.dao.UsuarioDAO;
import com.equipo3.reuneme.model.Token;
import com.equipo3.reuneme.model.Usuario;

@Service
public class UsuarioService {

	private Map<String, User> users = new HashMap<>();
	@Autowired
	public UsuarioDAO userDAO;
	@Autowired
	public TokenService tokenService;

	public String login(Map<String, Object> info) {
		String email = info.get("email").toString();
		String password = org.apache.commons.codec.digest.DigestUtils.sha512Hex(info.get("contrasena").toString());

		Usuario usuario = this.userDAO.findByEmail(email);

		if (usuario == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
		} else {

			if (usuario.getPwd().equals(password)) {

				String idToken = UUID.randomUUID().toString();
				Token token = new Token(idToken, usuario);
				return this.tokenService.generarToken(token);

			} else {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN);
			}
		}

	}

	public String findActivo(Map<String, Object> info) {
		Usuario usuario = this.userDAO.findByEmail(info.get("email").toString());
		return "" + usuario.getDesactivado();
	}

}