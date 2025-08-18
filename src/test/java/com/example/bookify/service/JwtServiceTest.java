package com.example.bookify.service;

import com.example.bookify.service.impl.JwtServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtServiceTest {

    private JwtServiceImpl jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl();

        String key = Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
        ReflectionTestUtils.setField(jwtService, "secretKey", key);

        userDetails = new User("testuser", "password", Collections.emptyList());
    }

    @Test
    void testGenerateAndExtractUsername() {
        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo(userDetails.getUsername());
    }

    @Test
    void testIsTokenValid_shouldReturnTrueForValidToken() {
        String token = jwtService.generateToken(userDetails);

        boolean valid = jwtService.isTokenValid(token, userDetails);

        assertThat(valid).isTrue();
    }

    @Test
    void testIsTokenValid_shouldReturnFalseForDifferentUser() {
        String token = jwtService.generateToken(userDetails);

        UserDetails anotherUser = new User("otheruser", "password", Collections.emptyList());

        boolean valid = jwtService.isTokenValid(token, anotherUser);

        assertThat(valid).isFalse();
    }

    @Test
    void testExtractClaim_customClaim() {
        String token = jwtService.generateToken(userDetails);

        Claims claims = jwtService.extractClaim(token, c -> c);

        assertThat(claims.getSubject()).isEqualTo(userDetails.getUsername());
        assertThat(claims.get("roles")).isNotNull();
    }

    @Test
    void testExpiredToken_shouldReturnFalseForExpiredToken() {
        String expiredToken = io.jsonwebtoken.Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setExpiration(new java.util.Date(System.currentTimeMillis() - 1000))
                .signWith(Keys.hmacShaKeyFor(
                        java.util.Base64.getDecoder().decode(
                                (String) ReflectionTestUtils.getField(jwtService, "secretKey")
                        )
                ))
                .compact();

        boolean valid = jwtService.isTokenValid(expiredToken, userDetails);

        assertThat(valid).isFalse();
    }
}
