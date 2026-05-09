package com.example.kimeduardfinalproject.security;

import com.example.kimeduardfinalproject.enums.KimEduardRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class KimEduardJwtUtil {
    private static final String SECRET =
            "ya-ne-hochy-pisat-SVOE-IMYA-NA-KAZHDOM-CLASSE-POZHALYISTA-YBEITE-MENYA-KimEduard";

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 часа

    private static Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public static String generateToken(String username, KimEduardRole role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    public static Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public static String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }
}