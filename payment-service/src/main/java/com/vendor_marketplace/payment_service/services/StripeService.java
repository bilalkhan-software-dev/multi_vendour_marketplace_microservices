package com.vendor_marketplace.payment_service.services;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

import java.util.List;
import java.util.Map;

public interface StripeService {
    Session createPaymentLinkSession(String orderId, List<String> sellerIds, String userId, String userEmail, Long totalAmount);

    boolean verifyStripe(String sessionId) throws StripeException;

}
