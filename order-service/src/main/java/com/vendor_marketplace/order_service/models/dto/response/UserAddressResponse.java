package com.vendor_marketplace.order_service.models.dto.response;

import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserAddressResponse {
    private Long id;
    private String name;
    private String locality;
    private String city;
    private String state;
    private String pinCode;
    private String mobile;
    private String address;

    private String userId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
