package com.vendor_marketplace.order_service.controller;

import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.order_service.handler.GenericResponseHandler;
import com.vendor_marketplace.order_service.models.dto.response.OrderResponse;
import com.vendor_marketplace.order_service.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/orders/admin")
@Tag(
        name = "Admin Order Management",
        description = "Admin endpoints for managing orders"
)
public class AdminOrderController {

    private final OrderService orderService;
    private final GenericResponseHandler response;

    @Operation(
            summary = "Get all orders",
            description = "Retrieve paginated list of all orders in the system"
    )
    @GetMapping
    ResponseEntity<?> getAllOrders(
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

        PagedResponse<OrderResponse> userOrders = orderService.getAllOrders(page, size, isNewest);
        return response.createBuildResponse("Orders retrieved successfully", userOrders, HttpStatus.OK);
    }

    @Operation(
            summary = "Get orders by order ID",
            description = "Retrieve all order items belonging to a specific order ID"
    )
    @GetMapping("/{orderId}")
    ResponseEntity<?> getOrdersOfTheOrderId(
            @Parameter(
                    description = "Order identifier",
                    required = true,
                    example = "ORD-2025001234-099"
            )
            @PathVariable @NotBlank(message = "Order ID is required") String orderId) {

        List<OrderResponse> orders = orderService.getOrdersOfTheOrderId(orderId);
        return response.createBuildResponse(String.format("Orders #%s retrieved successfully.", orderId), orders, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete orders by order ID",
            description = "Delete all order items associated with a specific order ID"
    )
    @DeleteMapping("/{orderId}")
    ResponseEntity<?> deleteOrdersOfTheOrderId(
            @Parameter(
                    description = "Order identifier",
                    required = true,
                    example = "ORD-2025001234-098"
            )
            @PathVariable @NotBlank(message = "Order ID is required") String orderId) {

        orderService.deleteOrders(orderId);
        return response.createBuildResponseMessage(
                String.format("Orders with ID #%s deleted successfully", orderId),
                HttpStatus.OK
        );
    }

    @Operation(
            summary = "Delete single order by ID",
            description = "Delete a specific order item by its unique identifier"
    )
    @DeleteMapping("/{id}/order")
    ResponseEntity<?> deleteOrderById(
            @Parameter(
                    description = "Order item unique ID",
                    required = true,
                    example = "12345"
            )
            @PathVariable @NotNull(message = "Order id is required") Long id) {

        orderService.deleteOrderById(id);
        return response.createBuildResponseMessage(
                String.format("Order #%s deleted successfully", id),
                HttpStatus.OK
        );
    }
}