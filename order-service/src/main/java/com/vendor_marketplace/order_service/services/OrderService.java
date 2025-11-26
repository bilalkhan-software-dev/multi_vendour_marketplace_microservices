package com.vendor_marketplace.order_service.services;

import com.vendor_marketplace.common.dto.enums.OrderStatus;
import com.vendor_marketplace.common.dto.enums.PaymentStatus;
import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.order_service.models.dto.request.CheckoutRequest;
import com.vendor_marketplace.order_service.models.dto.response.OrderResponse;
import com.vendor_marketplace.order_service.models.entity.Order;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public interface OrderService {
    String placeOrder(String userId, String email, CheckoutRequest request);

    List<OrderResponse> getOrdersOfTheOrderId(String orderId);

    OrderResponse getOrderById(Long id);

    PagedResponse<OrderResponse> getSellerOrders(String sellerId, int page, int size, boolean isNewest);

    PagedResponse<OrderResponse> getUserOrders(String userId, int page, int size, boolean isNewest);


    static <T, R> PagedResponse<R> buildPagedResponse(Page<T> page, Function<T, R> mapper) {
        return PagedResponse.<R>builder()
                .content(page.getContent().stream()
                        .map(mapper)
                        .toList())
                .isFirstPage(page.isFirst())
                .isLastPage(page.isLast())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    static boolean isNonCancellableStatus(Order order) {
        OrderStatus status = order.getOrderStatus();
        return status == OrderStatus.SHIPPED ||
                status == OrderStatus.DELIVERED ||
                status == OrderStatus.OUT_FOR_DELIVERY ||
                status == OrderStatus.CANCELLED ||
                status == OrderStatus.PAYMENT_FAILED;
    }

    static boolean isNotUpdateAbleStatus(Order order) {
        OrderStatus status = order.getOrderStatus();
        return status == OrderStatus.PENDING ||
                status == OrderStatus.CANCELLED ||
                status == OrderStatus.PAYMENT_FAILED;
    }

    void deleteOrderById(Long id);

    void deleteOrders(String orderId);

    PagedResponse<OrderResponse> getAllOrders(int page, int size, boolean isNewest);

    OrderResponse updateOrderStatus(String sellerId, Long id, OrderStatus newStatus);

    // for kafka
    void updateOrderAndPaymentStatus(String orderId, OrderStatus orderStatus, PaymentStatus paymentStatus, String email);

    List<OrderResponse> cancelOrder(String orderId, String userId, String email);
}
