package com.vendor_marketplace.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {

    private String id;

    private String userId;

    @Builder.Default
    private Set<CartItemResponse> cartItems = new HashSet<>();

    private double totalSellingPrice;
    private int totalItems;
    private int totalMrpPrice;
    private double discount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartItemResponse{

        private Long id;
        private Integer quantity;
        private Integer mrpPrice;
        private Integer sellingPrice;
        private String userId;
        private String cartId;
        private String productId;
        private String productSellerId;

    }
}
