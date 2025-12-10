package com.diegoflores.jwtapp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-in-ms}")
    private long jwtExpirationInMs;

    //Obtener la clave secreta en formato key
    private Key getSigninKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    //Generar token JWT
    public String generateToken(UserPrincipal principal){
        Date now = new Date();
        Date expiryDate = new Date (now.getTime() + jwtExpirationInMs);

        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return  Jwts.builder()
                .setSubject(principal.getUsername())           // username
                .claim("userId", principal.getId())           // ID del usuario
                .claim("roles", principal.getAuthorities())   // roles del usuario
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key) // firma segura HS512
                .compact();
    }

    //Validar token JWT
    public boolean validateToken(String token){

        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // token inválido, expirado o mal formado
        }
    }

    // 🔹 Obtener el username desde el token
    public String getUsernameFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // 🔹 Obtener el ID desde el token
    public Long getUserIdFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", Long.class);
    }
}
