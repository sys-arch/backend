package com.equipo3.reuneme.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.equipo3.reuneme.model.Usuario;


public interface UsuarioDAO extends JpaRepository<Usuario, String>{

	Usuario findByEmailAndPwd(String email, String pwd);
	Usuario findByEmail(String email);
	
}