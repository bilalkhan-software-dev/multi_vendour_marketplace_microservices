package com.vendor_marketplace.auth_service.controller;


import com.vendor_marketplace.auth_service.handler.GenericResponseHandler;
import com.vendor_marketplace.auth_service.models.dto.response.AuthUserResponse;
import com.vendor_marketplace.auth_service.service.AuthService;
import com.vendor_marketplace.common.dto.enums.AccountStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/admin/auth")
@RequiredArgsConstructor
@Tag(
        name = "Admin Authentication Management",
        description = "APIs for administrators to manage user authentication and accounts"
)
public class AdminAuthController {

    private final AuthService authService;
    private final GenericResponseHandler response;

    @Operation(
            summary = "Update Seller Account Status",
            description = "Admin endpoint to update the account status of a seller/user. ")
    @PatchMapping("/update/account/{id}")
    ResponseEntity<?> updateSellerAccountStatus(@RequestBody AccountStatus request, @PathVariable String id) {

        AuthUserResponse userResponse = authService.updateSellerAccountStatus(id, request);

        return response.createBuildResponse("Account status updated successfully!", userResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Get All Users",
            description = "Retrieve a paginated list of all authenticated users in the system. " +
                    "This endpoint supports pagination, sorting, and filtering. ")
    @GetMapping("/users")
    ResponseEntity<?> getUsers(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @RequestParam(required = false) String sort
    ) {
        List<AuthUserResponse> authUsers = authService.allAuthUsers(page, size, sort);
        return response.createBuildResponse("Auth users retrieved successfully!", authUsers, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete User by ID",
            description = "Permanently delete a user from the authentication system. " +
                    "This action cannot be undone. Only ADMIN users can perform this operation.")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUser(@PathVariable String id
    ) {
        authService.deleteAuthUserById(id);
        return response.createBuildResponseMessage("Deleted successfully!", HttpStatus.OK);
    }

    @Operation(
            summary = "Get User Details",
            description = "Retrieve detailed information about a specific user by their ID." +
                    "Includes authentication details, roles, and account status.")
    @GetMapping("/{id}")
    ResponseEntity<?> getDetails(@PathVariable String id
    ) {
        AuthUserResponse authUserById = authService.getAuthUserById(id);
        return response.createBuildResponse("Details retrieved successfully!", authUserById, HttpStatus.OK);

    }

}
