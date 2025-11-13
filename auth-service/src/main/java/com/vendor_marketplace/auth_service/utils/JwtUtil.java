package com.vendor_marketplace.auth_service.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.vendor_marketplace.auth_service.utils.Constants.JWT_TOKEN_EXPIRATION;

@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String jwtSecret;

    private SecretKey getKey() {
        byte[] key = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(key);
    }

    public String generateToken(String name, String email, String id, String role) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("name", name);
        claims.put("email", email);
        claims.put("role", role);

        return Jwts.builder()
                .claims().add(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .subject(id)
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_EXPIRATION))
                .and()
                .signWith(getKey())
                .compact();
    }

}
