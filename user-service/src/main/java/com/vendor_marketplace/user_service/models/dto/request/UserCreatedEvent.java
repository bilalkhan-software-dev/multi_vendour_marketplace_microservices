package com.vendor_marketplace.user_service.models.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreatedEvent {

    private String fullName;
    private String email;
    private String keycloakId;
    private String password;

}
