package com.foodapp.auth_service.security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

/**
 * Issues and validates JWTs using RS256 (asymmetric).
 * <p>
 * The auth-service is the sole token issuer: it signs with the RSA private key.
 * Every other service holds only the public key and can verify but never mint
 * tokens. In production, override {@code jwt.private-key}/{@code jwt.public-key}
 * to point at mounted secrets.
 */
@Component
public class JwtUtil {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final long expirationTime;

    public JwtUtil(
            @Value("${jwt.private-key}") Resource privateKeyResource,
            @Value("${jwt.public-key}") Resource publicKeyResource,
            @Value("${jwt.expiration}") long expirationTime
    ) {
        this.privateKey = PemKeyLoader.readPrivateKey(privateKeyResource);
        this.publicKey = PemKeyLoader.readPublicKey(publicKeyResource);
        this.expirationTime = expirationTime;
    }

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
