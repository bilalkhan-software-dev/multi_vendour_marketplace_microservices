package com.vendor_marketplace.order_service.services;

import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.order_service.models.dto.request.CheckoutRequest;
import com.vendor_marketplace.order_service.models.dto.response.OrderResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public interface OrderService {
    void placeOrder(String userId, CheckoutRequest request);

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

    void deleteOrderById(Long id);
}
