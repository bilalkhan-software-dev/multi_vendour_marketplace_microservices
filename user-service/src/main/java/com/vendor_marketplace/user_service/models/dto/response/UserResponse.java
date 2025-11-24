package com.vendor_marketplace.user_service.models.dto.response;


import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    private Set<UserAddressResponse> addresses = new HashSet<>();



}
