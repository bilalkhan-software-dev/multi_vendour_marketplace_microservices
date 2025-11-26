package com.vendor_marketplace.payment_service.services.Impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.vendor_marketplace.payment_service.config.StripeConfig;
import com.vendor_marketplace.payment_service.exception.PaymentException;
import com.vendor_marketplace.payment_service.exception.PaymentFailedException;
import com.vendor_marketplace.payment_service.services.StripeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
class StripeServiceImpl implements StripeService {

    private final StripeConfig stripeConfig;

    // Stripe minimum for PKR (approximately 141 PKR equivalent to $0.50 USD) according today
    private static final long STRIPE_MINIMUM_PKR = 141L; // 141 PKR minimum

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeConfig.getSecretKey();
    }

    @Override
    public Session createPaymentLinkSession(String orderId, List<String> sellerIds,
                                            String userId, String userEmail, Long totalAmountPKR) {

        // Input validation
        if (totalAmountPKR == null || totalAmountPKR <= 0) {
            throw new PaymentException("Order amount must be greater than zero");
        }

        // Validate minimum amount for PKR
        if (totalAmountPKR < STRIPE_MINIMUM_PKR) {
            throw new PaymentException(
                    String.format("Order amount Rs. %d is below Stripe's minimum requirement of Rs. %d",
                            totalAmountPKR, STRIPE_MINIMUM_PKR)
            );
        }

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(buildSuccessUrl(orderId))
                    .setCancelUrl(buildCancelUrl(orderId))
                    .setCustomerEmail(userEmail)
                    .addLineItem(createLineItem(orderId, totalAmountPKR))
                    .putMetadata("order_id", orderId)
                    .putMetadata("user_id", userId)
                    .putMetadata("order_email", userEmail)
                    .putMetadata("total_sellers", String.valueOf(sellerIds.size()))
                    .putMetadata("seller_ids", String.join(",", sellerIds))
                    .putMetadata("original_amount_pkr", totalAmountPKR.toString())
                    .build();

            Session session = Session.create(params);
            log.info("Stripe checkout session created. Session ID: {}, Order ID: {}, Amount: {} PKR",
                    session.getId(), orderId, totalAmountPKR);
            return session;

        } catch (StripeException e) {
            log.error("Failed to create Stripe checkout session for order: {}", orderId, e);
            throw new PaymentFailedException("Failed to create payment session: " + e.getMessage());
        }
    }

    @Override
    public boolean verifyStripe(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);

            if ("paid".equalsIgnoreCase(session.getPaymentStatus())) {
                log.info("Stripe payment verified successfully. Session: {}, Amount: {} {}",
                        sessionId, session.getAmountTotal(), session.getCurrency().toUpperCase());
                return true;
            }

            log.warn("Stripe payment not completed. Session: {}, Status: {}, Payment Status: {}",
                    sessionId, session.getStatus(), session.getPaymentStatus());
            return false;

        } catch (StripeException e) {
            log.error("Error verifying Stripe payment for session: {}", sessionId, e);
            throw new PaymentFailedException("Failed to verify payment: " + e.getMessage());
        }
    }

    private SessionCreateParams.LineItem createLineItem(String orderId, Long amountPKR) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("pkr")
                        .setUnitAmount(amountPKR)
                        .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setDescription("Vendor Marketplace Order")
                                .setName("Order #" + orderId)
                                .build())
                        .build())
                .build();
    }

    private String buildSuccessUrl(String orderId) {
        return stripeConfig.getSuccessUrl() + "?session_id={CHECKOUT_SESSION_ID}&order_id=" + orderId;
    }

    private String buildCancelUrl(String orderId) {
        return stripeConfig.getCancelUrl() + "?session_id={CHECKOUT_SESSION_ID}&order_id=" + orderId;
    }


}