package com.vendor_marketplace.auth_service.controller;

import com.vendor_marketplace.auth_service.models.dto.request.LoginRequest;
import com.vendor_marketplace.auth_service.models.dto.request.SellerRegisterRequest;
import com.vendor_marketplace.auth_service.models.dto.request.UserRegisterRequest;
import com.vendor_marketplace.auth_service.models.dto.response.AuthUserResponse;
import com.vendor_marketplace.auth_service.models.dto.response.LoginResponse;
import com.vendor_marketplace.auth_service.models.dto.response.RegistrationResponse;
import com.vendor_marketplace.auth_service.handler.GenericResponseHandler;
import com.vendor_marketplace.auth_service.service.AuthService;
import com.vendor_marketplace.common.dto.enums.AccountStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
@Slf4j
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

    @GetMapping("/users")
    ResponseEntity<?> getUsers(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @RequestParam(required = false) String sort
    ) {
        List<AuthUserResponse> authUsers = authService.allAuthUsers(page, size, sort);
        return response.createBuildResponse("Auth users retrieved successfully!", authUsers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUser(@PathVariable String id
    ) {
        authService.deleteAuthUserById(id);
        return response.createBuildResponseMessage("Deleted successfully!", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getDetails(@PathVariable String id
    ) {
        AuthUserResponse authUserById = authService.getAuthUserById(id);
        return response.createBuildResponse("Details retrieved successfully!", authUserById, HttpStatus.OK);

    }


    @GetMapping("/check-email")
    ResponseEntity<Boolean> checkEmailAvailability(@RequestParam String email) {
        boolean isAvailable = authService.validateEmail(email);
        return ResponseEntity.ok(isAvailable);
    }

    @PatchMapping("/update/account/{id}")
    ResponseEntity<?> updateSellerAccountStatus(@RequestBody AccountStatus request, @PathVariable String id) {

        AuthUserResponse userResponse = authService.updateSellerAccountStatus(id, request);

        return response.createBuildResponse("Account status updated successfully!", userResponse, HttpStatus.OK);
    }
}