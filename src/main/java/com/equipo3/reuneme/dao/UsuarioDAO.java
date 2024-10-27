package com.equipo3.reuneme.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.equipo3.reuneme.model.Usuario;
import java.util.List;


public interface UsuarioDAO extends JpaRepository<Usuario, String>{

	Usuario findByEmailAndPwd(String email, String pwd);
	Usuario findByEmail(String email);
	@Query("SELECT u.email FROM Usuario u")
    List<String> findAllEmails();
	
}