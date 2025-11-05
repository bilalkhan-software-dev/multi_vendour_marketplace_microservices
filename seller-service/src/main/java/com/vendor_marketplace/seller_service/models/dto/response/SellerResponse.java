package com.vendor_marketplace.seller_service.models.dto.response;

import com.vendor_marketplace.seller_service.models.entity.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerResponse {

    private Long sellerId;
    private String keyCloakId;
    private String name;
    private String mobile;
    private String email;
    private AccountStatus accountStatus;
    private SellerBankDetails bankDetails;
    private SellerBusinessDetails businessDetails;
    private SellerAddress pickupAddress;
    private String sales_tax_registration_number;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SellerBankDetails {
        private String accountNumber;
        private String bankName;
        private String accountHolderName;
        private String international_bank_account_number;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SellerBusinessDetails {
        private String businessName;
        private String businessAddress;
        private String businessMobileNumber;
        private String businessEmail;
        private String banner;
        private String logo;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SellerAddress {
        private String addressName;
        private String locality;
        private String city;
        private String state;
        private String postalCode;
        private String country;
        private String mobile;
        private String address;
    }
}
