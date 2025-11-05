package com.vendor_marketplace.user_service.controller;

import com.vendor_marketplace.user_service.handler.GenericResponseHandler;
import com.vendor_marketplace.user_service.models.dto.request.RegisterRequest;
import com.vendor_marketplace.user_service.models.dto.request.UpdateUserRequest;
import com.vendor_marketplace.user_service.models.dto.response.UserResponse;
import com.vendor_marketplace.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/user")
class UserController {

    private final UserService userService;
    private final GenericResponseHandler response;

    @PostMapping("/register")
    ResponseEntity<?> registerUserSyncMethod(@Valid @RequestBody RegisterRequest request) {

        UserResponse userResponse = userService.registerUser(request);

        if (userResponse == null) {
            return response.createBuildResponse("Something went wrong when creating user", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response.createBuildResponse("User registered successfully!", userResponse, HttpStatus.OK);
    }
    @PatchMapping("/update")
    ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequest request) {

        UserResponse userResponse = userService.updateUser(request);

        if (userResponse == null) {
            return response.createBuildResponse("Something went wrong when updating user", null, HttpStatus.BAD_REQUEST);
        }
        return response.createBuildResponse("User updated successfully!", userResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getUserDetails(@PathVariable Long id) {

        UserResponse userResponse = userService.getUserById(id);

        return response.createBuildResponse("User details retrieved successfully!", userResponse, HttpStatus.OK);
    }

    @GetMapping("/addresses/{id}")
    ResponseEntity<?> getUserAddresses(@PathVariable Long id) {

        Set<UserResponse.UserAddress> userAddresses = userService.getUserAddress(id);

        return response.createBuildResponse("User addresses retrieved successfully!", userAddresses, HttpStatus.OK);
    }

    @GetMapping("/users")
    ResponseEntity<?> getUsers() {

        List<UserResponse> allUsers = userService.getAllUsers();

        return response.createBuildResponse("Users retrieved successfully!", allUsers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return response.createBuildResponseMessage("User deleted successfully with id: " + id, HttpStatus.OK);
    }

}
