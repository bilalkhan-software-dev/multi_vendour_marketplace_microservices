package com.vendor_marketplace.order_service.models.dto.response;


import com.vendor_marketplace.common.dto.enums.OrderStatus;
import com.vendor_marketplace.common.dto.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;

    private String orderId;
    private String userId; // customer
    private String cartId;
    private String sellerId;

    @Builder.Default
    private List<OrderItemResponse> orderItems = new ArrayList<>();

    private Long addressId;

    private double totalMrpPrice;
    private Integer totalSellingPrice;
    private Integer totalItems;
    private Integer totalDiscount;

    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deliveryDate;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemResponse {

        private Long id;
        private String productId;

        private int quantity;
        private Integer mrpPrice;
        private Integer sellingPrice;


    }


}
