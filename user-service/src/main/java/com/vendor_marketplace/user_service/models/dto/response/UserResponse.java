package com.vendor_marketplace.user_service.models.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class UserResponse {

    private Long userId;
    private String fullName;
    private String email;
    private String mobile;
    private String authId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String role;

    @Builder.Default
    private Set<UserAddress> addresses = new HashSet<>();


    @Data
    @Builder
    public static class UserAddress{
        private Long id;
        private String name;
        private String locality;
        private String city;
        private String state;
        private String pinCode;
        private String mobile;
        private String address;
        private Long userId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }


}
