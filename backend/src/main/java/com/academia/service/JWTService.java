package com.academia.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
    private final SecretKey chaveSecreta;
    private final long expiracao;

    public JWTService(
            @Value("${jwt.secret}") String secret,
            @Value ("${jwt.expiracao}") long expiracao
    ) {
        this.chaveSecreta = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiracao = expiracao;
    }

    public String gerarToken(Authentication authentication) {
        Date agora = new Date();
        Date validade = new Date(agora.getTime() + expiracao);

        String role = authentication
                .getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("role",role)
                .setIssuedAt(agora)
                .setExpiration(validade)
                .signWith(chaveSecreta, SignatureAlgorithm.HS256)
                .compact();
    }
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(chaveSecreta)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
