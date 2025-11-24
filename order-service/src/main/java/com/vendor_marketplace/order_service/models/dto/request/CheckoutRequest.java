package com.vendor_marketplace.order_service.models.dto.request;

import com.vendor_marketplace.common.dto.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutRequest {

    @NotNull(message = "Address is required")
    private Long addressId;

    @NotBlank(message = "Payment method is required")
    private PaymentMethod paymentMethod;

}
