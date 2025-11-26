package com.vendor_marketplace.payment_service.services;

import com.stripe.exception.StripeException;
import com.vendor_marketplace.common.dto.enums.PaymentStatus;
import com.vendor_marketplace.common.dto.event.OrderCreatedEvent;
import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.payment_service.models.dto.response.PaymentResponse;

public interface PaymentService {

    void processPaymentCreation(OrderCreatedEvent event);

    String getPaymentLinkOfTheOrderId(String orderId);

    PaymentResponse getPaymentDetails(Long id);

    PaymentResponse getPaymentDetails(String orderId);

    PaymentResponse getPaymentDetailByPaymentSessionId(String paymentSessionId);

    void deletePaymentById(Long id);

    void deletePaymentByOrderId(String orderId);

    void verifyPaymentAndPublish(String paymentSessionId, String orderId, PaymentStatus paymentStatus) throws StripeException;

    boolean checkPaymentSessionIdStatus(String paymentSessionId) throws StripeException;

    PagedResponse<PaymentResponse> getAllPayments(int page, int size, boolean isNewest);
}
