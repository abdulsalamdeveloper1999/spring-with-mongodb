package com.asdevify.springWithMongo.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {

    private final SecretKey key;

    private static final long ACCESS_TOKEN_VALIDITY = 5 * 60 * 1000; // 5 minutes
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 days

    public JwtUtil() {
        // Use a static secret key, ideally stored securely and read from config/env
        String secret = "my-super-secret-key-that-is-long-enough-to-be-secure"; // example only
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // private SecretKey generateKey() {
    // try {
    // KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
    // return keyGenerator.generateKey();
    // } catch (NoSuchAlgorithmException e) {
    // throw new RuntimeException("Failed to generate key", e);
    // }
    // }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);

    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().claims(claims).subject(subject).header().empty().add("typ", "JWT").and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY)) // 5 minute
                .signWith(key, Jwts.SIG.HS256).compact();
    }

    private String refreshToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().claims(claims).subject(subject).header().empty().add("typ", "JWT").and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .signWith(key, Jwts.SIG.HS256).compact();
    }

    // use for authentication
    public String extractClaim(String token, String claim) {
        return extractAllClaims(token).get(claim, String.class);
    }

    public Claims extractAllClaims(String token) {
        try {
            Claims payload = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            return payload;
        } catch (Exception e) {
            log.error("Error extracting claims from token: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public String extractUsername(String token) {
        Claims allClaims = extractAllClaims(token);
        log.info(allClaims.toString());
        return allClaims.getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            
            return !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }

    }

}
