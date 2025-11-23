package com.vendor_marketplace.transaction_report_service.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vendor_marketplace.common.dto.enums.USER_ROLE;
import com.vendor_marketplace.transaction_report_service.handler.GenericResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.vendor_marketplace.common.constants.AuthHeaderConstant.CUSTOM_USER_ID_AUTHORIZATION_HEADER;
import static com.vendor_marketplace.common.constants.AuthHeaderConstant.CUSTOM_USER_ROLE_AUTHORIZATION_HEADER;

@Component
public class AuthHeaderFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String userIdHeader = request.getHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER);
        String roleHeader = request.getHeader(CUSTOM_USER_ROLE_AUTHORIZATION_HEADER);

        // Check if user ID header is present and valid
        if (userIdHeader == null || userIdHeader.trim().isEmpty()) {
            sendErrorResponse(response, "User ID header is required", HttpStatus.UNAUTHORIZED);
            return;
        }

        // Check if role header is present
        if (roleHeader == null || roleHeader.trim().isEmpty()) {
            sendErrorResponse(response, "Role header is required", HttpStatus.UNAUTHORIZED);
            return;
        }

        // Validate role - only Customer and ADMIN are allowed
        try {
            USER_ROLE role = USER_ROLE.valueOf(roleHeader);
            if (role != USER_ROLE.ROLE_SELLER && role != USER_ROLE.ROLE_ADMIN) {
                sendErrorResponse(response, "Invalid Role. Only Seller and ADMIN are allowed", HttpStatus.FORBIDDEN);
                return;
            }
        } catch (IllegalArgumentException e) {
            sendErrorResponse(response, "Invalid role format", HttpStatus.BAD_REQUEST);
            return;
        }

        // If all checks pass, continue with the filter chain
        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        GenericResponse genericResponse = GenericResponse.builder()
                .status("error")
                .data(message)
                .httpStatus(status)
                .build();

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), genericResponse);
    }
}