package com.vendor_marketplace.user_service.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @NotBlank(message = "User id is required")
    private Long id;

    @NotBlank(message = "Full name is required. Currently its empty")
    private String fullName;

}
