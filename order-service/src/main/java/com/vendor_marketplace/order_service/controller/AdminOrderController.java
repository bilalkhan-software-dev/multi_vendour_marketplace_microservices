package com.vendor_marketplace.order_service.controller;

import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.order_service.handler.GenericResponseHandler;
import com.vendor_marketplace.order_service.models.dto.response.OrderResponse;
import com.vendor_marketplace.order_service.services.OrderService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/orders/admin")
public class AdminOrderController {

    private final OrderService orderService;
    private final GenericResponseHandler response;


    @GetMapping
    ResponseEntity<?> getAllOrders(
            @RequestParam(required = false, defaultValue = "0")
            @Min(value = 0, message = "Page number cannot be negative") int page,
            @RequestParam(required = false, defaultValue = "10")
            @Min(value = 1, message = "Minimum page size is 1")
            @Max(value = 40, message = "Maximum page size is 40") int size,
            @RequestParam(required = false, defaultValue = "true") boolean isNewest) {

        PagedResponse<OrderResponse> userOrders = orderService.getAllOrders(page, size, isNewest);
        return response.createBuildResponse("Orders retrieved successfully", userOrders, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    ResponseEntity<?> getOrdersOfTheOrderId(
            @PathVariable @NotBlank(message = "Order ID is required") String orderId) {

        List<OrderResponse> orders = orderService.getOrdersOfTheOrderId(orderId);
        return response.createBuildResponse(String.format("Orders #%s retrieved successfully.", orderId), orders, HttpStatus.OK);
    }


    @DeleteMapping("/{orderId}")
    ResponseEntity<?> deleteOrdersOfTheOrderId(@PathVariable @NotBlank(message = "Order ID is required") String orderId) {

        orderService.deleteOrders(orderId);
        return response.createBuildResponseMessage(String.format("Orders with ID #%s deleted successfully", orderId), HttpStatus.OK);

    }

    @DeleteMapping("/{id}/order")
    ResponseEntity<?> deleteOrderById(@PathVariable @NotBlank(message = "Order id is required") Long id) {

        orderService.deleteOrderById(id);
        return response.createBuildResponseMessage(
                String.format("Order #%s deleted successfully", id),
                HttpStatus.OK);

    }


}
