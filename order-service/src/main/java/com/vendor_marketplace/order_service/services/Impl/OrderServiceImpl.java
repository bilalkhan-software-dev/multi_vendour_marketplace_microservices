package com.vendor_marketplace.order_service.services.Impl;

import com.vendor_marketplace.common.dto.enums.OrderStatus;
import com.vendor_marketplace.common.dto.enums.PaymentStatus;
import com.vendor_marketplace.common.dto.response.CartResponse;
import com.vendor_marketplace.common.utils.ProductUtil;
import com.vendor_marketplace.order_service.dao.interfaces.OrderDao;
import com.vendor_marketplace.order_service.dao.interfaces.OrderItemDao;
import com.vendor_marketplace.order_service.models.dto.request.CheckoutRequest;
import com.vendor_marketplace.order_service.models.entity.Order;
import com.vendor_marketplace.order_service.models.entity.OrderItem;
import com.vendor_marketplace.order_service.services.OrderService;
import com.vendor_marketplace.order_service.utils.OrderUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final OrderUtils utils;

    @Override
    @Transactional
    public void placeOrder(String userId, CheckoutRequest request) {
        log.info("Starting order placement process | userId={}", userId);

        log.debug("Fetching user cart | userId={}", userId);
        CartResponse cart = utils.fetchUserCart(userId);
        log.info("Cart retrieved successfully | userId={} | cartId={} | itemCount={}",
                userId, cart.getId(), cart.getCartItems().size());

        log.debug("Grouping cart items by seller | userId={}", userId);
        Map<String, List<CartResponse.CartItemResponse>> itemsBySeller = cart.getCartItems()
                .stream()
                .collect(Collectors.groupingBy(CartResponse.CartItemResponse::getProductSellerId));

        log.info("Items grouped by seller | userId={} | totalSellers={}",
                userId, itemsBySeller.size());

        String orderId = utils.generateOrderId();
        log.info("Generated master order ID | orderId={} | userId={}", orderId, userId);

        // Creating separate orders for each seller
        for (Map.Entry<String, List<CartResponse.CartItemResponse>> entry : itemsBySeller.entrySet()) {
            String sellerId = entry.getKey();
            List<CartResponse.CartItemResponse> sellerItems = entry.getValue();

            log.debug("Processing seller order | sellerId={} | itemCount={}",
                    sellerId, sellerItems.size());

            int totalQuantity = sellerItems.stream().mapToInt(CartResponse.CartItemResponse::getQuantity).sum();
            int totalMrpPrice = sellerItems.stream().mapToInt(CartResponse.CartItemResponse::getMrpPrice).sum();
            int totalSellingPrice = sellerItems.stream().mapToInt(CartResponse.CartItemResponse::getSellingPrice).sum();
            int totalDiscount = ProductUtil.calculateDiscountPercentage(totalMrpPrice, totalSellingPrice);

            log.info("Order pricing summary | sellerId={} | mrpPrice={} | sellingPrice={} | discount={}% | quantity={}",
                    sellerId, totalMrpPrice, totalSellingPrice, totalDiscount, totalQuantity);

            Order createdOrder = Order.builder()
                    .orderStatus(OrderStatus.PENDING)
                    .paymentStatus(PaymentStatus.PENDING)
                    .orderId(orderId)
                    .cartId(cart.getId())
                    .sellerId(sellerId)
                    .userId(userId)
                    .addressId(request.getAddressId())
                    .totalItems(totalQuantity)
                    .totalMrpPrice(totalMrpPrice)
                    .totalSellingPrice(totalSellingPrice)
                    .totalDiscount(totalDiscount)
                    .build();

            // Add order items
            log.debug("Adding items to order | orderId={} | sellerId={}", orderId, sellerId);
            for (CartResponse.CartItemResponse item : sellerItems) {
                createdOrder.addOrderItem(OrderItem.builder()
                        .sellingPrice(item.getSellingPrice())
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .mrpPrice(item.getMrpPrice())
                        .build());
                log.trace("Added item to order | productId={} | quantity={}",
                        item.getProductId(), item.getQuantity());
            }

            log.info("Saving order to database | orderId={} | sellerId={}", orderId, sellerId);
            orderDao.save(createdOrder);
            log.info("Order saved successfully | orderId={} | sellerId={}", orderId, sellerId);

            // Publish Kafka event for payment
            log.info("Publishing Kafka order created event | orderId={} | paymentMethod={}",
                    orderId, request.getPaymentMethod());
            // kafkaTemplate.send(...);
        }

        log.info("Order placement completed successfully | userId={} | masterOrderId={} | sellerCount={}",
                userId, orderId, itemsBySeller.size());
    }


}

