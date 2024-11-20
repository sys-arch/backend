package com.equipo3.reuneme.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.equipo3.reuneme.model.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    @Transactional
    void deleteByToken(String token); 

    @Transactional
    void deleteByEmail(String email); 
}
