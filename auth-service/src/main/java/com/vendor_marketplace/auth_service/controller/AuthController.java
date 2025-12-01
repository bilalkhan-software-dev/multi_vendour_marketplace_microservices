package com.vendor_marketplace.auth_service.controller;

import com.vendor_marketplace.auth_service.models.dto.request.LoginRequest;
import com.vendor_marketplace.auth_service.models.dto.request.SellerRegisterRequest;
import com.vendor_marketplace.auth_service.models.dto.request.UserRegisterRequest;
import com.vendor_marketplace.auth_service.models.dto.response.LoginResponse;
import com.vendor_marketplace.auth_service.models.dto.response.RegistrationResponse;
import com.vendor_marketplace.auth_service.handler.GenericResponseHandler;
import com.vendor_marketplace.auth_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "Public authentication endpoints for user registration and login"
)
public class AuthController {

    private final AuthService authService;
    private final GenericResponseHandler response;

    @Operation(
            summary = "Register new user",
            description = "Create a new customer account in the system"
    )
    @PostMapping("/register/user")
    ResponseEntity<?> registerUser(
            @Valid @RequestBody UserRegisterRequest request) {
        String userId = authService.registerUser(request);
        RegistrationResponse registrationResponse = RegistrationResponse.builder()
                .userId(userId)
                .email(request.getEmail())
                .build();
        return response.createBuildResponse("User registered successfully. You will confirmation email after complete process", registrationResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Register new seller",
            description = "Create a new seller account in the marketplace"
    )
    @PostMapping("/register/seller")
    ResponseEntity<?> registerSeller(
            @Valid @RequestBody SellerRegisterRequest request) {
        String userId = authService.registerSeller(request);
        RegistrationResponse registrationResponse = RegistrationResponse.builder()
                .userId(userId)
                .email(request.getEmail())
                .build();
        return response.createBuildResponse("Seller registered successfully. You will confirmation email after complete process!", registrationResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "User login",
            description = "Authenticate user with email and otp to obtain access token"
    )
    @PostMapping("/login")
    ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        LoginResponse loginResponse = authService.login(request);

        return response.createBuildResponse("Login Successfully!", loginResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Send OTP for login",
            description = "Send one-time password to user's email for authentication"
    )
    @GetMapping("/send/otp")
    ResponseEntity<?> sendOtp(@RequestParam String email) {

        authService.sendOTPForLogin(email);

        return response.createBuildResponseMessage(String.format("OTP sent successfully to your email: %s. \n The OTP will expired after 6 minutes", email), HttpStatus.OK);

    }

    @Operation(
            summary = "Check email availability",
            description = "Verify if an email address is available for registration"
    )
    @GetMapping("/check-email")
    ResponseEntity<Boolean> checkEmailAvailability(@RequestParam String email) {
        boolean isAvailable = authService.validateEmail(email);
        return ResponseEntity.ok(isAvailable);
    }

}