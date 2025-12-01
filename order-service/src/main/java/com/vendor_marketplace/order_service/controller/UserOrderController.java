package com.vendor_marketplace.order_service.controller;

import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.order_service.handler.GenericResponseHandler;
import com.vendor_marketplace.order_service.models.dto.request.CheckoutRequest;
import com.vendor_marketplace.order_service.models.dto.response.OrderItemResponse;
import com.vendor_marketplace.order_service.models.dto.response.OrderResponse;
import com.vendor_marketplace.order_service.services.OrderItemService;
import com.vendor_marketplace.order_service.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.vendor_marketplace.common.constants.AuthHeaderConstant.CUSTOM_USER_EMAIL_AUTHORIZATION_HEADER;
import static com.vendor_marketplace.common.constants.AuthHeaderConstant.CUSTOM_USER_ID_AUTHORIZATION_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/orders/user")
@Tag(
        name = "User Order Management",
        description = "Endpoints for users to manage their orders"
)
public class UserOrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final GenericResponseHandler response;

    @Operation(
            summary = "Place new order",
            description = "Create a new order by checking out cart items"
    )
    @PostMapping("/place")
    ResponseEntity<?> checkout(
            @Parameter(
                    description = "User ID from authentication header",
                    required = true,
                    hidden = true
            )
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId,

            @Parameter(
                    description = "User email from authentication header",
                    required = true,
                    hidden = true
            )
            @RequestHeader(CUSTOM_USER_EMAIL_AUTHORIZATION_HEADER) String email,

            @Valid @RequestBody CheckoutRequest checkoutRequest
    ) {

        String orderId = orderService.placeOrder(userId, email, checkoutRequest);
        return response.createBuildResponseMessage(
                String.format("Order #%s created successfully. Please wait while we create payment request.", orderId),
                HttpStatus.ACCEPTED
        );
    }

    @Operation(
            summary = "Cancel order",
            description = "Cancel an existing order by order ID"
    )
    @PutMapping("/{orderId}/cancel")
    ResponseEntity<?> cancelOrder(
            @Parameter(
                    description = "User ID from authentication header",
                    required = true,
                    hidden = true
            )
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId,

            @Parameter(
                    description = "User email from authentication header",
                    required = true,
                    hidden = true
            )
            @RequestHeader(CUSTOM_USER_EMAIL_AUTHORIZATION_HEADER) String email,

            @Parameter(
                    description = "Order ID to cancel",
                    required = true,
                    example = "ORD-2025001234-099"
            )
            @PathVariable @NotBlank(message = "Order ID is required") String orderId) {

        List<OrderResponse> cancelledOrder = orderService.cancelOrder(orderId, userId, email);
        return response.createBuildResponse(
                String.format("Order #%s cancelled successfully.", orderId),
                cancelledOrder,
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Get user orders",
            description = "Retrieve paginated list of orders for the authenticated user"
    )
    @GetMapping
    ResponseEntity<?> getOrders(
            @Parameter(
                    description = "User ID from authentication header",
                    required = true,
                    hidden = true
            )
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId,

            @Parameter(
                    description = "Page number (zero-based)",
                    example = "0"
            )
            @RequestParam(required = false, defaultValue = "0")
            @Min(value = 0, message = "Page number cannot be negative") int page,

            @Parameter(
                    description = "Number of items per page (max 40)",
                    example = "10"
            )
            @RequestParam(required = false, defaultValue = "10")
            @Min(value = 1, message = "Minimum page size is 1")
            @Max(value = 40, message = "Maximum page size is 40") int size,

            @Parameter(
                    description = "Sort by newest first",
                    example = "true"
            )
            @RequestParam(required = false, defaultValue = "true") boolean isNewest) {

        PagedResponse<OrderResponse> userOrders = orderService.getUserOrders(userId, page, size, isNewest);
        return response.createBuildResponse("Your orders retrieved successfully", userOrders, HttpStatus.OK);
    }

    @Operation(
            summary = "Get order details",
            description = "Retrieve detailed information about a specific order"
    )
    @GetMapping("/{id}")
    ResponseEntity<?> getOrderDetailById(
            @Parameter(
                    description = "Order ID",
                    required = true,
                    example = "12345"
            )
            @PathVariable @NotNull(message = "Order id is required") Long id) {

        OrderResponse order = orderService.getOrderById(id);
        return response.createBuildResponse("Order detail retrieved successfully", order, HttpStatus.OK);
    }

    @Operation(
            summary = "Get order item details",
            description = "Retrieve detailed information about a specific order item"
    )
    @GetMapping("/{id}/item")
    ResponseEntity<?> getOrderItemDetailById(
            @Parameter(
                    description = "Order item ID",
                    required = true,
                    example = "54321"
            )
            @PathVariable @NotNull(message = "Order id is required") Long id) {

        OrderItemResponse item = orderItemService.findById(id);
        return response.createBuildResponse("Order item detail retrieved successfully", item, HttpStatus.OK);
    }
}