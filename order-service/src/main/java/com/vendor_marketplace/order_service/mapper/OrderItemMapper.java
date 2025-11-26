package com.vendor_marketplace.order_service.mapper;

import com.vendor_marketplace.order_service.models.dto.response.OrderItemResponse;
import com.vendor_marketplace.order_service.models.entity.OrderItem;

public class OrderItemMapper {

    public static OrderItemResponse toOrderItemResponse(OrderItem orderItem) {

        if (orderItem == null) {
            return null;
        }

        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProductId())
                .order(orderItem.getOrder() != null ?
                        OrderItemResponse.OrderResponse.builder()
                                .id(orderItem.getOrder().getId())
                                .orderId(orderItem.getOrder().getOrderId())
                                .userId(orderItem.getOrder().getUserId())
                                .cartId(orderItem.getOrder().getCartId())
                                .sellerId(orderItem.getOrder().getSellerId())
                                .addressId(orderItem.getOrder().getAddressId())
                                .totalMrpPrice(orderItem.getOrder().getTotalMrpPrice())
                                .totalSellingPrice(orderItem.getOrder().getTotalSellingPrice())
                                .totalItems(orderItem.getOrder().getTotalItems())
                                .totalDiscount(orderItem.getOrder().getTotalDiscount())
                                .orderStatus(orderItem.getOrder().getOrderStatus())
                                .paymentStatus(orderItem.getOrder().getPaymentStatus())
                                .createdAt(orderItem.getOrder().getCreatedAt())
                                .updatedAt(orderItem.getOrder().getUpdatedAt())
                                .deliveryDate(orderItem.getOrder().getDeliveryDate())
                                .build() :
                        null)
                .quantity(orderItem.getQuantity())
                .mrpPrice(orderItem.getMrpPrice())
                .sellingPrice(orderItem.getSellingPrice())
                .build();
    }
}