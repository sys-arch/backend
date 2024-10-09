package com.equipo3.reuneme.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.equipo3.reuneme.model.Empleado;

public interface EmpleadoDAO extends JpaRepository<Empleado, String>{

    Empleado findByEmail (String email);
    Empleado findByEmailAndPwd(String email, String pwd);
	
}