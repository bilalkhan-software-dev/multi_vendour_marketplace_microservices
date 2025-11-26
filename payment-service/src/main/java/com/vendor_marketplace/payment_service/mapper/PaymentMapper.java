package com.vendor_marketplace.payment_service.mapper;

import com.vendor_marketplace.payment_service.models.dto.response.PaymentResponse;
import com.vendor_marketplace.payment_service.models.entity.Payment;

public class PaymentMapper {

    public static PaymentResponse toPaymentResponse(Payment payment) {

        if (payment == null) {
            return null;
        }

        return PaymentResponse.builder()
                .id(payment.getId())
                .paymentStatus(payment.getPaymentStatus())
                .paymentSessionId(payment.getPaymentSessionId())
                .paymentLinkUrl(payment.getPaymentLinkUrl())
                .paymentMethod(payment.getPaymentMethod())
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .userEmail(payment.getUserEmail())
                .totalAmount(payment.getTotalAmount())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .sellerIds(payment.getSellerIds())
                .build();
    }
}
