package com.equipo3.reuneme.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

	@Service
	public class LoginAttemptService {

	    private static final int MAX_ATTEMPTS = 3;
	    private static final long LOCK_TIME_DURATION = TimeUnit.MINUTES.toMillis(15); // Bloqueo por 15 minutos

	    private final ConcurrentHashMap<String, AttemptInfo> attemptsCache = new ConcurrentHashMap<>();

	    public void loginSucceeded(String email) {
	        attemptsCache.remove(email); // Resetea el contador de intentos en caso de éxito
	    }

	    public void loginFailed(String email) {
	        AttemptInfo attemptInfo = attemptsCache.getOrDefault(email, new AttemptInfo(0, System.currentTimeMillis()));

	        int attempts = attemptInfo.getAttempts() + 1;
	        long lockTime = System.currentTimeMillis();

	        attemptsCache.put(email, new AttemptInfo(attempts, lockTime));
	    }

	    public boolean isBlocked(String email) {
	        if (!attemptsCache.containsKey(email)) {
	            return false;
	        }

	        AttemptInfo attemptInfo = attemptsCache.get(email);
	        if (attemptInfo.getAttempts() < MAX_ATTEMPTS) {
	            return false;
	        }

	        // Verificar si el tiempo de bloqueo ya expiró
	        long lockTime = attemptInfo.getLockTime();
	        if ((System.currentTimeMillis() - lockTime) > LOCK_TIME_DURATION) {
	            attemptsCache.remove(email); // Reinicia el bloqueo si ya expiró
	            return false;
	        }

	        return true; // Bloqueado
	    }

	    public void checkAndThrowIfBlocked(String email) {
	        if (isBlocked(email)) {
	            throw new RuntimeException("Algo ha pasado, inténtelo más tarde.");
	        }
	    }

	    // Clase auxiliar para almacenar información de intentos
	    private static class AttemptInfo {
	        private final int attempts;
	        private final long lockTime;

	        public AttemptInfo(int attempts, long lockTime) {
	            this.attempts = attempts;
	            this.lockTime = lockTime;
	        }

	        public int getAttempts() {
	            return attempts;
	        }

	        public long getLockTime() {
	            return lockTime;
	        }
	    }
	}


