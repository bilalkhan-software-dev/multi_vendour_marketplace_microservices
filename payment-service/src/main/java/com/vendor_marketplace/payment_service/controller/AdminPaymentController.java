package com.vendor_marketplace.payment_service.controller;

import com.stripe.exception.StripeException;
import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.payment_service.handler.GenericResponseHandler;
import com.vendor_marketplace.payment_service.models.dto.response.PaymentResponse;
import com.vendor_marketplace.payment_service.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/admin/payment")
@Tag(
        name = "Admin Payment Management",
        description = "Admin endpoints for managing payment records"
)
public class AdminPaymentController {

    private final PaymentService paymentService;
    private final GenericResponseHandler response;

    @Operation(
            summary = "Get all payments",
            description = "Retrieve paginated list of all payment records"
    )
    @GetMapping("/all")
    ResponseEntity<?> getAllPayments(
            @Parameter(
                    description = "Page number (zero-based)",
                    example = "0"
            )
            @RequestParam(required = false, defaultValue = "0") int page,

            @Parameter(
                    description = "Number of items per page (max 40)",
                    example = "20"
            )
            @RequestParam(required = false, defaultValue = "20")
            @Max(value = 40, message = "Maximum size of page is 40") int size,

            @Parameter(
                    description = "Sort by newest first",
                    example = "true"
            )
            @RequestParam(required = false, defaultValue = "true") boolean isNewest
    ) {

        PagedResponse<PaymentResponse> payments = paymentService.getAllPayments(page, size, isNewest);

        return response.createBuildResponse("All payments retrieved successfully!", payments, HttpStatus.OK);

    }

    @Operation(
            summary = "Get payment by ID",
            description = "Retrieve payment details by payment record ID"
    )
    @GetMapping("/{id}")
    ResponseEntity<?> getPaymentDetails(
            @Parameter(
                    description = "Payment record ID",
                    required = true,
                    example = "12345"
            )
            @PathVariable @NotBlank(message = "ID is required") Long id) {

        PaymentResponse paymentDetails = paymentService.getPaymentDetails(id);

        return response.createBuildResponse("Payment details retrieved successfully!", paymentDetails, HttpStatus.OK);

    }

    @Operation(
            summary = "Get payment by order ID",
            description = "Retrieve payment details by order ID"
    )
    @GetMapping("")
    ResponseEntity<?> getPaymentDetailsByOrder(
            @Parameter(
                    description = "Order ID",
                    required = true,
                    example = "ORD-2025001234-098"
            )
            @RequestParam @NotBlank(message = "Order ID is required") String orderId) {

        PaymentResponse paymentDetails = paymentService.getPaymentDetails(orderId);

        return response.createBuildResponse("Payment details retrieved successfully!", paymentDetails, HttpStatus.OK);

    }

    @Operation(
            summary = "Get payment by session ID",
            description = "Retrieve payment details by payment session ID"
    )
    @GetMapping("/session")
    ResponseEntity<?> getPaymentDetailByPaymentSessionId(
            @Parameter(
                    description = "Payment session ID",
                    required = true,
                    example = "cs_test_a1b2c3d4e5f6g7h8i9j0"
            )
            @RequestParam @NotBlank(message = "Payment Session Id is required") String paymentSessionId) {

        PaymentResponse paymentDetails = paymentService.getPaymentDetailByPaymentSessionId(paymentSessionId);

        return response.createBuildResponse("Payment details retrieved successfully!", paymentDetails, HttpStatus.OK);

    }

    @Operation(
            summary = "Delete payment by ID",
            description = "Delete a payment record by its ID"
    )
    @DeleteMapping("/{id}")
    ResponseEntity<?> deletePaymentById(
            @Parameter(
                    description = "Payment record ID",
                    required = true,
                    example = "12345"
            )
            @PathVariable @NotBlank(message = "ID is required") Long id) {

        paymentService.deletePaymentById(id);

        return response.createBuildResponseMessage("Payment deleted successfully with id: " + id, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete payment by order ID",
            description = "Delete payment record by order ID"
    )
    @DeleteMapping("/{orderId}/order")
    ResponseEntity<?> deletePaymentById(
            @Parameter(
                    description = "Order ID",
                    required = true,
                    example = "ORD-2025001234-099"
            )
            @PathVariable @NotBlank(message = "Order ID is required") String orderId) {

        paymentService.deletePaymentByOrderId(orderId);

        return response.createBuildResponseMessage("Payment deleted successfully with Order ID: " + orderId, HttpStatus.OK);
    }

    @Operation(
            summary = "Check payment status",
            description = "Check the status of a payment session"
    )
    @GetMapping("/check")
    ResponseEntity<?> checkPaymentStatus(
            @Parameter(
                    description = "Payment session ID",
                    required = true,
                    example = "cs_test_a1b2c3d4e5f6g7h8i9j0"
            )
            @RequestParam @NotBlank(message = "Payment Session Id is required") String paymentSessionId) throws StripeException {
        boolean checked = paymentService.checkPaymentSessionIdStatus(paymentSessionId);

        return response.createBuildResponse("Payment session status fetched successfully!", checked, checked ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

}