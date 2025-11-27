package com.vendor_marketplace.gateway.filter;

import com.vendor_marketplace.gateway.service.User_SellerValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import static com.vendor_marketplace.gateway.handler.security.JsonServerAuthenticationEntryPoint.getResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthFilter implements WebFilter {

    private final User_SellerValidationService validationService;

    @Value("${jwt.secret.key}")
    private String secretKey;

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/api/v2/auth/send/otp",
            "/api/v2/auth/check-email",
            "/api/v2/auth/register/user",
            "/api/v2/auth/register/seller",
            "/api/v2/auth/login",
            "/api/v2/products/query",
            "/api/v2/public",
            "/actuator/health",
            "/swagger-ui/",
            "/v3/api-docs/"
    );

    private boolean isPublicPath(ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().value();
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private ReactiveJwtDecoder getJwtDecoder() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA384");
        return NimbusReactiveJwtDecoder.withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS384)
                .build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        log.info("[JWTAuthFilter] Incoming request: path={}, method={}", path, exchange.getRequest().getMethod());

        if (isPublicPath(exchange)) {
            log.info("[JWTAuthFilter] Public path, skipping JWT check: {}", path);
            return chain.filter(exchange);
        }

        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        log.info("[JWTAuthFilter] Authorization header: {}", token);

        if (token == null || !token.startsWith("Bearer ")) {
            log.warn("[JWTAuthFilter] Missing or invalid Authorization header");
            return writeJsonError(exchange, HttpStatus.UNAUTHORIZED, "MISSING_TOKEN", "Authorization header missing or invalid.");
        }

        String jwtToken = token.replace("Bearer ", "").trim();
        ReactiveJwtDecoder decoder = getJwtDecoder();

        return decoder.decode(jwtToken)
                .flatMap(jwt -> {
                    String userId = jwt.getSubject();
                    String role = (String) jwt.getClaims().get("role");
                    String email = (String) jwt.getClaims().get("email");

                    log.info("[JWTAuthFilter] JWT decoded: userId={}, role={}, email={}", userId, role, email);

                    return validateUserInDatabase(userId, role)
                            .flatMap(isValid -> {
                                if (!isValid) {
                                    log.warn("[JWTAuthFilter] User/Seller not found: userId={}, role={}", userId, role);
                                    return writeJsonError(exchange, HttpStatus.FORBIDDEN, "USER_NOT_FOUND", "User or seller not found.");
                                }

                                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
                                UsernamePasswordAuthenticationToken auth =
                                        new UsernamePasswordAuthenticationToken(userId, null, authorities);

                                ServerHttpRequest mutated = exchange.getRequest().mutate()
                                        .header("X-User-ID", userId)
                                        .header("X-User-Roles", role)
                                        .header("X-User-Email", email)
                                        .build();

                                log.info("[JWTAuthFilter] Forwarding request with headers: X-User-ID={}, X-User-Roles={}, X-User-Email={}", userId, role, email);

                                return chain.filter(exchange.mutate().request(mutated).build())
                                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                            });
                })
                .onErrorResume(JwtException.class, e -> {
                    String message = e.getMessage() != null ? e.getMessage() : "Invalid JWT";
                    log.error("[JWTAuthFilter] JWT verification failed: {}", message);

                    if (message.toLowerCase().contains("expired")) {
                        return writeJsonError(exchange, HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED", "JWT token has expired. Please log in again.");
                    } else {
                        return writeJsonError(exchange, HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "JWT token is invalid or malformed.");
                    }
                })
                .onErrorResume(e -> {
                    log.error("[JWTAuthFilter] Unexpected error: {}", e.getMessage(), e);
                    return writeJsonError(exchange, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "An unexpected error occurred.");
                });
    }

    private Mono<Boolean> validateUserInDatabase(String authId, String role) {
        if (role.contains("ROLE_SELLER")) {
            return validationService.validateSellerAuthId(authId).onErrorReturn(false);
        } else if (role.contains("ROLE_CUSTOMER") || role.contains("ROLE_ADMIN")) {
            return validationService.validateUserAuthId(authId).onErrorReturn(false);
        }
        return Mono.just(false);
    }

    private Mono<Void> writeJsonError(ServerWebExchange exchange, HttpStatus status, String code, String message) {
        return getResponse(exchange, status, code, message);
    }
}
