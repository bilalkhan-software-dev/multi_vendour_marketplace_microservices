package com.vendor_marketplace.payment_service.controller;

import com.stripe.exception.StripeException;
import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.payment_service.handler.GenericResponseHandler;
import com.vendor_marketplace.payment_service.models.dto.response.PaymentResponse;
import com.vendor_marketplace.payment_service.services.PaymentService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/admin/payment")
public class AdminPaymentController {

    private final PaymentService paymentService;
    private final GenericResponseHandler response;

    @GetMapping("/all")
    ResponseEntity<?> getAllPayments(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") @Max(value = 40, message = "Maximum size of page is 40") int size,
            @RequestParam(required = false, defaultValue = "true") boolean isNewest
    ) {

        PagedResponse<PaymentResponse> payments = paymentService.getAllPayments(page, size, isNewest);

        return response.createBuildResponse("All payments retrieved successfully!", payments, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    ResponseEntity<?> getPaymentDetails(@PathVariable @NotBlank(message = "ID is required") Long id) {

        PaymentResponse paymentDetails = paymentService.getPaymentDetails(id);

        return response.createBuildResponse("Payment details retrieved successfully!", paymentDetails, HttpStatus.OK);

    }

    @GetMapping("")
    ResponseEntity<?> getPaymentDetailsByOrder(@RequestParam @NotBlank(message = "Order ID is required") String orderId) {

        PaymentResponse paymentDetails = paymentService.getPaymentDetails(orderId);

        return response.createBuildResponse("Payment details retrieved successfully!", paymentDetails, HttpStatus.OK);

    }

    @GetMapping("")
    ResponseEntity<?> getPaymentDetailByPaymentSessionId(@RequestParam @NotBlank(message = "Payment Session Id is required") String paymentSessionId) {

        PaymentResponse paymentDetails = paymentService.getPaymentDetailByPaymentSessionId(paymentSessionId);

        return response.createBuildResponse("Payment details retrieved successfully!", paymentDetails, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deletePaymentById(@PathVariable @NotBlank(message = "ID is required") Long id) {

        paymentService.deletePaymentById(id);

        return response.createBuildResponseMessage("Payment deleted successfully with id: " + id, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}/order")
    ResponseEntity<?> deletePaymentById(@PathVariable @NotBlank(message = "Order ID is required") String orderId) {

        paymentService.deletePaymentByOrderId(orderId);

        return response.createBuildResponseMessage("Payment deleted successfully with Order ID: " + orderId, HttpStatus.OK);
    }

    @GetMapping("/check")
    ResponseEntity<?> checkPaymentStatus(@RequestParam @NotBlank(message = "Payment Session Id is required") String paymentSessionId) throws StripeException {
        boolean checked = paymentService.checkPaymentSessionIdStatus(paymentSessionId);

        return response.createBuildResponse("Payment session status fetched successfully!", checked, checked ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

}
