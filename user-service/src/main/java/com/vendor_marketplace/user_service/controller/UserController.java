package com.vendor_marketplace.user_service.controller;

import com.vendor_marketplace.common.dto.event.UserCreatedEvent;
import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.user_service.handler.GenericResponseHandler;
import com.vendor_marketplace.user_service.models.dto.request.AddressRequest;
import com.vendor_marketplace.user_service.models.dto.request.UpdateUserRequest;
import com.vendor_marketplace.user_service.models.dto.response.UserAddressResponse;
import com.vendor_marketplace.user_service.models.dto.response.UserResponse;
import com.vendor_marketplace.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/user")
class UserController {

    private final UserService userService;
    private final GenericResponseHandler response;

    @PostMapping("/register")
    ResponseEntity<?> registerUserSyncMethod(@Valid @RequestBody UserCreatedEvent request) {

        UserResponse userResponse = userService.registerUser(request);

        if (userResponse == null) {
            return response.createBuildResponse("Something went wrong when creating user", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response.createBuildResponse("User registered successfully!", userResponse, HttpStatus.OK);
    }


    @PutMapping("/add/address/{id}")
    ResponseEntity<?> addAddressToUser(@PathVariable Long id, @Valid @RequestBody AddressRequest address) {

        UserResponse userResponse = userService.addAddressToUser(id, address);

        return response.createBuildResponse("Address added successfully!", userResponse, HttpStatus.OK);
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

    @GetMapping("/addresses/{userId}")
    ResponseEntity<?> getUserAddresses(@PathVariable Long userId) {

        Set<UserAddressResponse> userAddresses = userService.getUserAddress(userId);

        return response.createBuildResponse("User addresses retrieved successfully!", userAddresses, HttpStatus.OK);
    }

    @GetMapping("/users")
    ResponseEntity<?> getUsers(@RequestParam(required = false, defaultValue = "0") Integer pageNo) {

        PagedResponse<UserResponse> allUsers = userService.getAllUsers(pageNo);

        return response.createBuildResponse("Users retrieved successfully!", allUsers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return response.createBuildResponseMessage("User deleted successfully with id: " + id, HttpStatus.OK);
    }

    @GetMapping("/validate/id/{id}")
    ResponseEntity<Boolean> isUserExistById(@PathVariable Long id) {
        boolean userExistWithById = userService.isUserExistWithById(id);
        return ResponseEntity.ok(userExistWithById);
    }

    @GetMapping("/validate/{id}")
    ResponseEntity<Boolean> isUserExistAuthUserId(@PathVariable String id) {
        boolean userExistWithById = userService.isUserExistWithById(id);
        return ResponseEntity.ok(userExistWithById);
    }

    @GetMapping("/{id}/address")
    ResponseEntity<UserAddressResponse> getAddressById(@PathVariable Long id) {
        UserAddressResponse address = userService.getAddressById(id);
        return ResponseEntity.ok(address);
    }


}
