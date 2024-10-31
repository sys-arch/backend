package com.equipo3.reuneme.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

import com.equipo3.reuneme.model.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenDAO extends MongoRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    @Transactional
    void deleteByToken(String token); 

    @Transactional
    void deleteByEmail(String email); 
}