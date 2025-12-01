package com.vendor_marketplace.order_service.controller;

import com.vendor_marketplace.common.dto.enums.OrderStatus;
import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.order_service.handler.GenericResponseHandler;
import com.vendor_marketplace.order_service.models.dto.response.OrderResponse;
import com.vendor_marketplace.order_service.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.vendor_marketplace.common.constants.AuthHeaderConstant.CUSTOM_USER_ID_AUTHORIZATION_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/orders/seller")
@Tag(
        name = "Seller Order Management",
        description = "Endpoints for sellers to manage their orders"
)
public class SellerOrderController {

    private final OrderService orderService;
    private final GenericResponseHandler response;

    @Operation(
            summary = "Get seller orders",
            description = "Retrieve paginated list of orders for the authenticated seller"
    )
    @GetMapping
    ResponseEntity<?> getSellerOrders(
            @Parameter(
                    description = "Seller ID from authentication header",
                    required = true,
                    hidden = true
            )
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String sellerId,

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

        PagedResponse<OrderResponse> sellerOrders = orderService.getSellerOrders(sellerId, page, size, isNewest);
        return response.createBuildResponse("Your orders retrieved successfully", sellerOrders, HttpStatus.OK);
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
            @PathVariable Long id) {

        OrderResponse order = orderService.getOrderById(id);
        return response.createBuildResponse("Order detail retrieved successfully", order, HttpStatus.OK);
    }

    @Operation(
            summary = "Update order status",
            description = "Update the status of a specific order"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateOrderStatus(
            @Parameter(
                    description = "Seller ID from authentication header",
                    required = true,
                    hidden = true
            )
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String sellerId,

            @Parameter(
                    description = "Order ID",
                    required = true,
                    example = "12345"
            )
            @PathVariable @NotNull(message = "Order id is required") Long id,

            @Parameter(
                    description = "New order status",
                    required = true,
                    example = "PROCESSING"
            )
            @RequestParam @NotNull(message = "Order status is required") OrderStatus status) {

        OrderResponse updatedOrder = orderService.updateOrderStatus(sellerId, id, status);
        return response.createBuildResponse(
                String.format("Order #%s status updated to: %s", id, status.name()),
                updatedOrder,
                HttpStatus.OK
        );
    }
}