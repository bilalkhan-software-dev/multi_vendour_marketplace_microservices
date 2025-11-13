package com.vendor_marketplace.common.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerCreatedEvent {

    private String name;

    private String mobile;
    private String authId;

    private String email;

    private SellerBankDetails bankDetails;

    private SellerBusinessDetails businessDetails;

    private SellerAddress pickupAddress;

    private String STRN;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SellerBankDetails {
        private String accountNumber;
        private String bankName;
        private String accountHolderName;
        private String IBAN;
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
        private String address; // Location where the order will deliver
    }
}
