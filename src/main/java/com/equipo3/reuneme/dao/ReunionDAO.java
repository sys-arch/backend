package com.equipo3.reuneme.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.equipo3.reuneme.model.Reunion;

public interface ReunionDAO extends JpaRepository<Reunion, Long> {

}
