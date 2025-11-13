package com.vendor_marketplace.auth_service.mapper;

import com.vendor_marketplace.auth_service.models.dto.response.AuthUserResponse;
import com.vendor_marketplace.auth_service.models.entity.AuthUser;

public class AuthUserMapper {

    public static AuthUserResponse toAuthUserResponse(AuthUser authUser) {
        return AuthUserResponse.builder()
                .id(authUser.getId())
                .email(authUser.getEmail())
                .fullName(authUser.getFullName())
                .role(authUser.getRole())
                .accountStatus(authUser.getAccountStatus())
                .createdAt(authUser.getCreatedAt().toString())
                .updatedAt(authUser.getUpdatedAt().toString())
                .build();
    }

}
