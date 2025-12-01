package com.vendor_marketplace.order_service.controller;

import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.order_service.handler.GenericResponseHandler;
import com.vendor_marketplace.order_service.models.dto.request.CheckoutRequest;
import com.vendor_marketplace.order_service.models.dto.response.OrderItemResponse;
import com.vendor_marketplace.order_service.models.dto.response.OrderResponse;
import com.vendor_marketplace.order_service.services.OrderItemService;
import com.vendor_marketplace.order_service.services.OrderService;
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
public class UserOrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final GenericResponseHandler response;


    @PostMapping("/place")
    ResponseEntity<?> checkout(
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId,
            @RequestHeader(CUSTOM_USER_EMAIL_AUTHORIZATION_HEADER) String email,
            @Valid @RequestBody CheckoutRequest checkoutRequest
    ) {

        String orderId = orderService.placeOrder(userId, email, checkoutRequest);
        return response.createBuildResponseMessage(String.format("Order #%s created successfully. Please wait while we create payment request.", orderId), HttpStatus.ACCEPTED);

    }

    @PutMapping("/{orderId}/cancel")
    ResponseEntity<?> cancelOrder(
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId,
            @RequestHeader(CUSTOM_USER_EMAIL_AUTHORIZATION_HEADER) String email,
            @PathVariable @NotBlank(message = "Order ID is required") String orderId) {

        List<OrderResponse> cancelledOrder = orderService.cancelOrder(orderId, userId, email);
        return response.createBuildResponse(String.format("Order #%s cancelled successfully.", orderId), cancelledOrder, HttpStatus.OK);
    }


    @GetMapping
    ResponseEntity<?> getOrders(
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId,
            @RequestParam(required = false, defaultValue = "0")
            @Min(value = 0, message = "Page number cannot be negative") int page,
            @RequestParam(required = false, defaultValue = "10")
            @Min(value = 1, message = "Minimum page size is 1")
            @Max(value = 40, message = "Maximum page size is 40") int size,
            @RequestParam(required = false, defaultValue = "true") boolean isNewest) {

        PagedResponse<OrderResponse> userOrders = orderService.getUserOrders(userId, page, size, isNewest);
        return response.createBuildResponse("Your orders retrieved successfully", userOrders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getOrderDetailById(@PathVariable @NotNull(message = "Order id is required") Long id) {

        OrderResponse order = orderService.getOrderById(id);
        return response.createBuildResponse("Order detail retrieved successfully", order, HttpStatus.OK);
    }


    @GetMapping("/{id}/item")
    ResponseEntity<?> getOrderItemDetailById(@PathVariable @NotNull(message = "Order id is required") Long id) {

        OrderItemResponse item = orderItemService.findById(id);
        return response.createBuildResponse("Order item detail retrieved successfully", item, HttpStatus.OK);
    }


}
