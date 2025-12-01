package com.vendor_marketplace.auth_service.controller;


import com.vendor_marketplace.auth_service.handler.GenericResponseHandler;
import com.vendor_marketplace.auth_service.models.dto.response.AuthUserResponse;
import com.vendor_marketplace.auth_service.service.AuthService;
import com.vendor_marketplace.common.dto.enums.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AuthService authService;
    private final GenericResponseHandler response;

    @PatchMapping("/update/account/{id}")
    ResponseEntity<?> updateSellerAccountStatus(@RequestBody AccountStatus request, @PathVariable String id) {

        AuthUserResponse userResponse = authService.updateSellerAccountStatus(id, request);

        return response.createBuildResponse("Account status updated successfully!", userResponse, HttpStatus.OK);
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

}
