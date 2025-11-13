package com.vendor_marketplace.auth_service.models.dto.response;


import com.vendor_marketplace.common.dto.enums.AccountStatus;
import com.vendor_marketplace.common.dto.enums.USER_ROLE;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class AuthUserResponse {

    private UUID id;

    private String fullName;

    private String email;

    private USER_ROLE role;

    private String createdAt;

    private String updatedAt;

    private AccountStatus accountStatus;


}
