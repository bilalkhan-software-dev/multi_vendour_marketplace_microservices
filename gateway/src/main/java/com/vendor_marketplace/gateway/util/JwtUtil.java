package com.vendor_marketplace.gateway.util;

import com.vendor_marketplace.gateway.exception.InvalidTokenException;
import com.vendor_marketplace.gateway.exception.JwtTokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private SecretKey getKey() {
        byte[] key = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(key);
    }

    public Claims extractAllClaims(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring("Bearer ".length());
            }
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new JwtTokenExpiredException("Token is expired");
        } catch (JwtException e) {
            throw new InvalidTokenException("JWT token is invalid");
        } catch (Exception e) {
            log.error("Error when extracting claims: {}", e.getMessage());
            throw new InvalidTokenException("Failed to process token");
        }
    }

    public boolean isTokenExpired(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration().before(new Date());
    }

    public String extractAuthUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public String getRolesFromToken(String token) {
        Claims claims = extractAllClaims(token);
         return claims.get("roles").toString();
    }

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        Object emailClaim = claims.get("email");
        return emailClaim != null ? emailClaim.toString() : "";
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}