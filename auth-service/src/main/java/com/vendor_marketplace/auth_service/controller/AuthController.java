package com.vendor_marketplace.auth_service.controller;

import com.vendor_marketplace.auth_service.models.dto.request.LoginRequest;
import com.vendor_marketplace.auth_service.models.dto.request.SellerRegisterRequest;
import com.vendor_marketplace.auth_service.models.dto.request.UserRegisterRequest;
import com.vendor_marketplace.auth_service.models.dto.response.LoginResponse;
import com.vendor_marketplace.auth_service.models.dto.response.RegistrationResponse;
import com.vendor_marketplace.auth_service.handler.GenericResponseHandler;
import com.vendor_marketplace.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final GenericResponseHandler response;

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

    @PostMapping("/login")
    ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        LoginResponse loginResponse = authService.login(request);

        return response.createBuildResponse("Login Successfully!", loginResponse, HttpStatus.OK);
    }

    @GetMapping("/send/otp")
    ResponseEntity<?> sendOtp(@RequestParam String email) {

        authService.sendOTPForLogin(email);

        return response.createBuildResponseMessage(String.format("OTP sent successfully to your email: %s. \n The OTP will expired after 6 minutes", email), HttpStatus.OK);

    }
    @GetMapping("/check-email")
    ResponseEntity<Boolean> checkEmailAvailability(@RequestParam String email) {
        boolean isAvailable = authService.validateEmail(email);
        return ResponseEntity.ok(isAvailable);
    }

}