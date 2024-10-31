package com.equipo3.reuneme.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.equipo3.reuneme.model.Usuario;

public interface UsuarioDAO extends MongoRepository<Usuario, String>{

	Usuario findByEmailAndPwd(String email, String pwd);
	Usuario findByEmail(String email);
	
}