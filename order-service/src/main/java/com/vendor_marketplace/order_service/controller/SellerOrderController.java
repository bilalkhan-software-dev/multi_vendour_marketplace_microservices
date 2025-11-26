package com.vendor_marketplace.order_service.controller;


import com.vendor_marketplace.common.dto.enums.OrderStatus;
import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.order_service.handler.GenericResponseHandler;
import com.vendor_marketplace.order_service.models.dto.response.OrderResponse;
import com.vendor_marketplace.order_service.services.OrderService;
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
@RequestMapping("/api/v2/seller/orders")
public class SellerOrderController {

    private final OrderService orderService;
    private final GenericResponseHandler response;

    @GetMapping
    ResponseEntity<?> getSellerOrders(
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String sellerId,
            @RequestParam(required = false, defaultValue = "0")
            @Min(value = 0, message = "Page number cannot be negative") int page,
            @RequestParam(required = false, defaultValue = "10")
            @Min(value = 1, message = "Minimum page size is 1")
            @Max(value = 40, message = "Maximum page size is 40") int size,
            @RequestParam(required = false, defaultValue = "true") boolean isNewest) {

        PagedResponse<OrderResponse> sellerOrders = orderService.getSellerOrders(sellerId, page, size, isNewest);
        return response.createBuildResponse("Your orders retrieved successfully", sellerOrders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getOrderDetailById(@PathVariable Long id) {

        OrderResponse order = orderService.getOrderById(id);
        return response.createBuildResponse("Order detail retrieved successfully", order, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateOrderStatus(
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String sellerId,
            @PathVariable @NotNull(message = "Order id is required") Long id,
            @RequestParam @NotNull(message = "Order status is required") OrderStatus status) {

        OrderResponse updatedOrder = orderService.updateOrderStatus(sellerId, id, status);
        return response.createBuildResponse(
                String.format("Order #%s status updated to: %s", id, status.name()),
                updatedOrder,
                HttpStatus.OK
        );
    }



}

