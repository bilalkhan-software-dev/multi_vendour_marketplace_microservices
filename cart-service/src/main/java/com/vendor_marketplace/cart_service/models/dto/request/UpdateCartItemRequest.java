package com.vendor_marketplace.cart_service.models.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCartItemRequest {

    @Min(value = 1,message = "Minimum one quantity is required")
    private Integer quantity;


}
