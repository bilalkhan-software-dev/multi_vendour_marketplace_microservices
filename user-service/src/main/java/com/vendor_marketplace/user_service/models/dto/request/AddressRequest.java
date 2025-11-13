package com.vendor_marketplace.user_service.models.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s.'-]+$", message = "Name can only contain letters, spaces, apostrophes, and hyphens")
    private String name;

    @NotBlank(message = "Locality is required")
    @Size(min = 2, max = 100, message = "Locality must be between 2 and 100 characters")
    private String locality;

    @NotBlank(message = "City is required")
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s.-]+$", message = "City can only contain letters, spaces, dots, and hyphens")
    private String city;

    @NotBlank(message = "State is required")
    @Size(min = 2, max = 50, message = "State must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s.-]+$", message = "State can only contain letters, spaces, dots, and hyphens")
    private String state;

    @NotBlank(message = "PIN code is required")
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "PIN code must be 6 digits and cannot start with 0")
    private String pinCode;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^(\\+92|0)?3[0-9]{2}[0-9]{7}$", message = "Mobile number must be a valid Pakistani number (03XXXXXXXXX or +923XXXXXXXXX)")
    private String mobile;

    @NotBlank(message = "Address is required")
    @Size(min = 10, max = 255, message = "Address must be between 10 and 255 characters")
    private String address;
}