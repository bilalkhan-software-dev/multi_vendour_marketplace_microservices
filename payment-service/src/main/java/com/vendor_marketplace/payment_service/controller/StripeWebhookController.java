package com.vendor_marketplace.payment_service.controller;


import com.stripe.exception.StripeException;
import com.vendor_marketplace.common.dto.enums.PaymentStatus;
import com.vendor_marketplace.payment_service.handler.GenericResponseHandler;
import com.vendor_marketplace.payment_service.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/webhook/stripe")
@Slf4j
public class StripeWebhookController {

    private final PaymentService paymentService;
    private final GenericResponseHandler response;

    @PutMapping("/success")
    ResponseEntity<?> processSuccess(
            @RequestParam("session_id") String paymentSessionId,
            @RequestParam String order_id
    ) throws StripeException {
        log.info("Stripe success callback received for payment: {}", paymentService);
        paymentService.verifyPaymentAndPublish(paymentSessionId, order_id, PaymentStatus.SUCCESS);
        return response.createBuildResponse("Payment verified successfully. Thanks for using our service.", order_id, HttpStatus.OK);
    }

    @PutMapping("/success")
    ResponseEntity<?> processCancel(
            @RequestParam("session_id") String paymentSessionId,
            @RequestParam String order_id
    ) throws StripeException {
        log.info("Stripe cancel callback received for payment: {}", paymentService);
        paymentService.verifyPaymentAndPublish(paymentSessionId, order_id, PaymentStatus.CANCEL);
        return response.createErrorResponse("Payment cancelled successfully. Your order is not placed", order_id, HttpStatus.OK);
    }
}