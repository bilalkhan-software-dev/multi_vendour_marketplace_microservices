package com.vendor_marketplace.auth_service.controller;


import com.vendor_marketplace.auth_service.dto.request.SellerRegisterRequest;
import com.vendor_marketplace.auth_service.dto.request.UserRegisterRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v2/auth")
public class AuthController {

    @PostMapping("/register/user")
    ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register/seller")
    ResponseEntity<?> registerSeller(@Valid @RequestBody SellerRegisterRequest sellerRegisterRequest) {

        return ResponseEntity.ok().build();
    }

}
