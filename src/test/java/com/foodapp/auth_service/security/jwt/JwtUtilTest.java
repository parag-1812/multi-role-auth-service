package com.foodapp.auth_service.security.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercises the RS256 issue/verify round trip using the committed dev keypair.
 * This also proves the bundled private and public PEMs are a matching pair, which
 * is the guarantee the verifier services (e.g. order-engine) rely on.
 */
class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil(
            new ClassPathResource("keys/jwt-private.pem"),
            new ClassPathResource("keys/jwt-public.pem"),
            86_400_000L);

    @Test
    void generatesAndValidatesToken() {
        String token = jwtUtil.generateToken("alice", "USER");

        assertTrue(jwtUtil.isTokenValid(token));
        assertEquals("alice", jwtUtil.extractUsername(token));
        assertEquals("USER", jwtUtil.extractRole(token));
    }

    @Test
    void rejectsTamperedToken() {
        String token = jwtUtil.generateToken("alice", "USER");
        String tampered = token.substring(0, token.length() - 3) + "xyz";

        assertFalse(jwtUtil.isTokenValid(tampered));
    }
}
