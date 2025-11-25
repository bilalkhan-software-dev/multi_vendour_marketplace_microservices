package com.vendor_marketplace.payment_service.models.dto.response;


import com.vendor_marketplace.common.dto.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class PaymentResponse {

    public Long id;

    private Long totalAmount;

    private PaymentStatus paymentStatus;

    private String paymentMethod;
    private String paymentLinkId;

    @Builder.Default
    private List<String> sellerIds = new ArrayList<>();

    private String userId;
    private String orderId;
    private String userEmail;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
