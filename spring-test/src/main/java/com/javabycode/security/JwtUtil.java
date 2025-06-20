package com.javabycode.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;

@Component
public class JwtUtil {
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("mysecretkeymysecretkeymysecretkey12".getBytes());
    private static final long EXPIRATION = 1000 * 60 * 60; // 1 hora

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String validateToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // 3 partes do token: header, payload, signature
    // header: algoritmo de assinatura e tipo do token
    // payload: informações do usuário (claims)
    // signature: assinatura do token para verificar a autenticidade
    public String generateTestToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("authorities", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }
}
