package com.vendor_marketplace.gateway.config;

import com.vendor_marketplace.gateway.filter.JWTAuthFilter;
import com.vendor_marketplace.gateway.handler.security.JsonServerAccessDeniedHandler;
import com.vendor_marketplace.gateway.handler.security.JsonServerAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.EnableWebFlux;


@EnableWebFlux
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http,
            JWTAuthFilter jwtAuthFilter,
            JsonServerAuthenticationEntryPoint authEntryPoint,
            JsonServerAccessDeniedHandler accessDeniedHandler) {

        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/api/v2/auth/register/**",
                                "/api/v2/auth/login",
                                "/api/v2/auth/send/otp",
                                "/api/v2/auth/check-email/**",
                                "/actuator/health",
                                "/swagger-ui/**",
                                "/api/v2/products/query/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .pathMatchers("/api/v2/admin/**").hasRole("ADMIN")
                        .pathMatchers("/api/v2/products/command/**").hasAnyRole("SELLER","ADMIN")
                        .pathMatchers("/api/v2/seller/**").hasAnyRole("SELLER", "ADMIN")
                        .pathMatchers("/api/v2/user/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .anyExchange().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .build();
    }

}