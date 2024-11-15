package com.equipo3.reuneme.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);


    @Value("${jwt.secret}")
    private String secretKey; // Llave secreta (definida en application.properties)

    private final long expirationTime = 86400000; // Tiempo de expiración (1 día)

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Generar JWT
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.replace("ROLE_", "")) // Añadir el rol al token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Validar JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            logger.info("Token validated successfully.");

            return true;
        } catch (Exception e) {
            logger.error("Token validation failed: " + e.getMessage());

            return false;
        }
    }

    // Obtener nombre de usuario (subject) del JWT
    public String getUsernameFromToken(String token) {
        String username = getClaimFromToken(token, Claims::getSubject);
        logger.info("Username extracted from token: " + username);
        return username;
    }

    public String getRoleFromToken(String token) {
        String role = getClaimFromToken(token, claims -> claims.get("role", String.class));
        logger.info("Role extracted from token: " + role);
        return role;
    }
    // Método genérico para obtener cualquier dato del token
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }
}
