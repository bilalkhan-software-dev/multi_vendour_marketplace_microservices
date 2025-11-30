package com.vendor_marketplace.user_service.service;


import com.vendor_marketplace.common.dto.event.UserCreatedEvent;
import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.user_service.models.dto.request.AddressRequest;
import com.vendor_marketplace.user_service.models.dto.request.UpdateUserRequest;
import com.vendor_marketplace.user_service.models.dto.response.UserAddressResponse;
import com.vendor_marketplace.user_service.models.dto.response.UserResponse;
import com.vendor_marketplace.user_service.models.entity.Address;
import com.vendor_marketplace.user_service.models.entity.User;
import jakarta.transaction.Transactional;

import java.util.Set;
import java.util.stream.Collectors;


public interface UserService {


    @Transactional
    UserResponse registerUser(UserCreatedEvent request);

    UserResponse getUserById(Long id);

    @Transactional
    void deleteUser(Long userId);

    @Transactional
    UserResponse addAddressToUser(Long userId, AddressRequest request);

    Set<UserAddressResponse> getUserAddress(Long userId);

    @Transactional
    UserResponse updateUser(UpdateUserRequest request);

    boolean isUserExistWithById(Long id);

    boolean isUserExistWithById(String id);

    PagedResponse<UserResponse> getAllUsers(Integer pageNo);

    UserAddressResponse getAddressById(Long id);

    static UserResponse buildUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .authId(user.getAuthId())
                .mobile(user.getMobile() != null ? user.getMobile() : "")
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .addresses(user.getAddresses().stream().map(UserService::buildUserAddressResponse)
                        .collect(Collectors.toSet()))
                .build();
    }

    static UserAddressResponse buildUserAddressResponse(Address address) {
        return UserAddressResponse.builder()
                .userId(address.getUser().getAuthId())
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

    static Address toEntity(AddressRequest request) {
        return Address.builder()
                .name(request.getName())
                .city(request.getCity())
                .state(request.getState())
                .pinCode(request.getPinCode())
                .mobile(request.getMobile())
                .locality(request.getLocality())
                .address(request.getAddress())
                .build();
    }

}
