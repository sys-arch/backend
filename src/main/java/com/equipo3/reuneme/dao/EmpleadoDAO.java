package com.equipo3.reuneme.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.equipo3.reuneme.model.Empleado;

public interface EmpleadoDAO extends MongoRepository<Empleado, String>{

    Empleado findByEmail (String email);
    Empleado findByEmailAndPwd(String email, String pwd);
    List<Empleado> findAll();
	
}