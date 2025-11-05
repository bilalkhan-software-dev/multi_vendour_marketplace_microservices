package com.vendor_marketplace.user_service.service;


import com.vendor_marketplace.user_service.dao.interfaces.UserDao;
import com.vendor_marketplace.user_service.exception.ExistDataException;
import com.vendor_marketplace.user_service.exception.ResourceNotFoundException;
import com.vendor_marketplace.user_service.models.dto.request.RegisterRequest;
import com.vendor_marketplace.user_service.models.dto.request.UpdateUserRequest;
import com.vendor_marketplace.user_service.models.dto.response.UserResponse;
import com.vendor_marketplace.user_service.models.entity.Address;
import com.vendor_marketplace.user_service.models.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserDao userDao;

    @Transactional
    public UserResponse registerUser(RegisterRequest request) {
        log.info("Registering User with : {}", request.getEmail());
        Boolean emailExist = userDao.checkKeycloakOrEmailExist(request.getKeycloakId(),request.getEmail());

        if (emailExist) {
            log.warn("Registering user already exist : {}", request.getEmail());
            throw new ExistDataException(String.format("User with email: %s or keycloak_id: %s already exist", request.getEmail(),request.getKeycloakId()));
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(request.getPassword())
                .keyCloakId(request.getKeycloakId())
                .build();

        User saved = userDao.saveUser(user);
        return buildUserResponse(saved);
    }

    public UserResponse getUserById(Long id) {
        log.info("Fetching User with id : {}", id);
        return buildUserResponse(userDao.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User with id %s not found", id))
        ));
    }

    @Transactional
    public void deleteUser(Long userId){
        log.info("Deleting user with id : {}", userId);
        Boolean exists = userDao.existsById(userId);
        if (exists) {
            userDao.deleteUserById(userId);
            log.info("Deleted successfully user with id : {}", userId);
        }else {
            log.warn("User with id : {} not found", userId);
            throw new ResourceNotFoundException(String.format("User with id %s not found", userId));
        }
    }

    public Set<UserResponse.UserAddress> getUserAddress(Long userId){

        List<Address> addressesByUserId = userDao.getAddressesByUserId(userId);

       return addressesByUserId.stream().map(this::buildUserAddressResponse).collect(Collectors.toSet());
    }

    @Transactional
    public UserResponse updateUser(UpdateUserRequest request){

      User existingUser = userDao.findById(request.getId()).orElseThrow(
              () -> new ResourceNotFoundException(String.format("User with id %s not found", request.getId()))
      );
      existingUser.setFullName(request.getFullName());
    return buildUserResponse(userDao.saveUser(existingUser));

    }

    public List<UserResponse> getAllUsers(){
        return userDao
                .getAllUsers()
                .stream()
                .map(this::buildUserResponse)
                .collect(Collectors.toList());
    }







    private UserResponse buildUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .keyCloakId(user.getKeyCloakId())
                .role(user.getRole().toString())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .addresses(user.getAddress().size() > 0 ?
                        user.getAddress().stream().map(address -> buildUserAddressResponse(address))
                                .collect(Collectors.toSet()) :
                         Collections.emptySet())
                .build();
    }

    private UserResponse.UserAddress buildUserAddressResponse(Address address){
        return UserResponse.UserAddress.builder()
                .userId(address.getUserId())
                .name(address.getName())
                .city(address.getCity())
                .state(address.getState())
                .pinCode(address.getPinCode())
                .mobile(address.getMobile())
                .id(address.getId())
                .locality(address.getLocality())
                .createdAt(address.getCreatedAt())
                .updatedAt(address.getUpdatedAt())
                .build();
    }





}
