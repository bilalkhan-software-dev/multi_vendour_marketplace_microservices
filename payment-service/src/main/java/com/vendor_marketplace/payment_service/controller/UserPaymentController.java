package com.vendor_marketplace.payment_service.controller;


import com.vendor_marketplace.payment_service.handler.GenericResponseHandler;
import com.vendor_marketplace.payment_service.services.PaymentService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/payment")
public class UserPaymentController {

    private final PaymentService paymentService;
    private final GenericResponseHandler response;


    @GetMapping("/link")
    ResponseEntity<?> getPaymentLink(@RequestParam @NotBlank(message = "ORDER ID is required") String orderId) {

        String link = paymentService.getPaymentLinkOfTheOrderId(orderId);

        return response.createBuildResponse("This is your payment link open this in browser to proceed order confirmation." +
                "Please Don't share this link to anyone for security purpose.", link, HttpStatus.OK);
    }

}
