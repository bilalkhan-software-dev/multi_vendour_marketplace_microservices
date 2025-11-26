package com.vendor_marketplace.order_service.mapper;

import com.vendor_marketplace.order_service.models.dto.response.OrderResponse;
import com.vendor_marketplace.order_service.models.entity.Order;

import java.util.ArrayList;

public class OrderMapper {

    public static OrderResponse toOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .cartId(order.getCartId())
                .sellerId(order.getSellerId())
                .orderItems(order.getOrderItems() != null ?
                        order.getOrderItems().stream()
                                .map(orderItem -> OrderResponse.OrderItemResponse.builder()
                                        .id(orderItem.getId())
                                        .productId(orderItem.getProductId())
                                        .quantity(orderItem.getQuantity())
                                        .mrpPrice(orderItem.getMrpPrice())
                                        .sellingPrice(orderItem.getSellingPrice())
                                        .build())
                                .toList() :
                        new ArrayList<>())
                .addressId(order.getAddressId())
                .totalMrpPrice(order.getTotalMrpPrice())
                .totalSellingPrice(order.getTotalSellingPrice())
                .totalItems(order.getTotalItems())
                .totalDiscount(order.getTotalDiscount())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .deliveryDate(order.getDeliveryDate())
                .build();
    }
}