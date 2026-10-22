package com.lucianodev.sistemaagendamento.configuration;

import com.lucianodev.sistemaagendamento.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64URL.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Usuario usuario){
        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("role", usuario.getRole().name())
                .claim("userId", usuario.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();

    }

    public String extractEmail(String token){
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token){
        return extractClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token){
        try {
            extractClaims(token);
            return true;
        }catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }

    private Claims extractClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
