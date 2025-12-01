package com.vendor_marketplace.order_service.services.Impl;

import com.vendor_marketplace.common.dto.enums.OrderStatus;
import com.vendor_marketplace.common.dto.enums.PaymentStatus;
import com.vendor_marketplace.common.dto.event.OrderCreatedEvent;
import com.vendor_marketplace.common.dto.event.ProductUpdateStockEvent;
import com.vendor_marketplace.common.dto.event.SellerReportCreateEvent;
import com.vendor_marketplace.common.dto.event.SendNotificationEvent;
import com.vendor_marketplace.common.dto.response.CartResponse;
import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.common.exception.UnauthorizedException;
import com.vendor_marketplace.common.helper.EmailSendingTemplate;
import com.vendor_marketplace.common.utils.ProductUtil;
import com.vendor_marketplace.order_service.dao.interfaces.OrderDao;
import com.vendor_marketplace.order_service.exception.BusinessException;
import com.vendor_marketplace.order_service.mapper.OrderMapper;
import com.vendor_marketplace.order_service.models.dto.request.CheckoutRequest;
import com.vendor_marketplace.order_service.models.dto.response.OrderResponse;
import com.vendor_marketplace.order_service.models.entity.Order;
import com.vendor_marketplace.order_service.models.entity.OrderItem;
import com.vendor_marketplace.order_service.kafka.publisher.KafkaPublisherService;
import com.vendor_marketplace.order_service.services.OrderService;
import com.vendor_marketplace.order_service.utils.OrderUtils;
import jakarta.validation.ValidationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final KafkaPublisherService kafkaPublisherService;
    private final OrderUtils utils;

    @Override
    @Transactional
    public String placeOrder(String userId, String email, CheckoutRequest request) {
        log.info("Starting order placement process | userId={}", userId);

        log.debug("Fetching user cart | userId={}", userId);
        CartResponse cart = utils.fetchUserCart(userId);
        log.info("Cart retrieved successfully | userId={} | cartId={} | itemCount={}",
                userId, cart.getId(), cart.getCartItems().size());

        if (cart.getCartItems().isEmpty()) {
            throw new BusinessException("Your cart is empty. Add product to cart before placing an order");
        }

        if (cart.getTotalSellingPrice() < 142) {
            throw new BusinessException("Order is not place because your amount is too low");
        }

        log.debug("Grouping cart items by seller | userId={}", userId);
        Map<String, List<CartResponse.CartItemResponse>> itemsBySeller = cart.getCartItems()
                .stream()
                .collect(Collectors.groupingBy(CartResponse.CartItemResponse::getProductSellerId));

        log.info("Items grouped by seller | userId={} | totalSellers={}",
                userId, itemsBySeller.size());

        String orderId = utils.generateOrderId();
        log.info("Generated master order ID | orderID={} | userId={}", orderId, userId);

        List<String> sellerIds = new ArrayList<>();

        // Creating separate orders for each seller
        for (Map.Entry<String, List<CartResponse.CartItemResponse>> entry : itemsBySeller.entrySet()) {
            String sellerId = entry.getKey();
            List<CartResponse.CartItemResponse> sellerItems = entry.getValue();
            sellerIds.add(sellerId);

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
            log.debug("Adding items to order | orderID={} | sellerId={}", orderId, sellerId);
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

            log.info("Saving order to database | orderID={} | sellerId={}", orderId, sellerId);
            orderDao.save(createdOrder);
            log.info("Order saved successfully | orderID={} | sellerId={}", orderId, sellerId);
        }

        log.info("Publishing Kafka order created event | orderID={} | paymentMethod={}",
                orderId, request.getPaymentMethod());
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .paymentMethod(request.getPaymentMethod())
                .orderId(orderId)
                .customerId(userId)
                .customerEmail(email)
                .totalAmount(cart.getCartItems().stream().mapToInt(CartResponse.CartItemResponse::getSellingPrice).sum())
                .sellerIds(sellerIds)
                .build();
        kafkaPublisherService.publishOrderCreatedEvent(event);
        log.info("Order placement completed successfully | userId={} | masterOrderID={} | sellerCount={}",
                userId, orderId, itemsBySeller.size());
        return orderId;
    }


    @Override
    public List<OrderResponse> getOrdersOfTheOrderId(String orderId) {
        return orderDao.findByOrderId(orderId).stream()
                .map(OrderMapper::toOrderResponse)
                .toList();
    }

    @Override
    public OrderResponse getOrderById(Long id) {

        return OrderMapper.toOrderResponse(orderDao.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order not found")
        ));
    }

    @Override
    public PagedResponse<OrderResponse> getSellerOrders(String sellerId, int page, int size, boolean isNewest) {
        Page<@NonNull Order> orders = orderDao.findBySeller(sellerId, page, size, isNewest);
        return OrderService.buildPagedResponse(orders, OrderMapper::toOrderResponse);
    }

    @Override
    public PagedResponse<OrderResponse> getUserOrders(String userId, int page, int size, boolean isNewest) {
        Page<@NonNull Order> orders = orderDao.findByUser(userId, page, size, isNewest);
        return OrderService.buildPagedResponse(orders, OrderMapper::toOrderResponse);
    }

    @Override
    @Transactional
    public void deleteOrderById(Long id) {
        log.info("Deleting order with id | order Id={}", id);
        orderDao.deleteById(id);
        log.info("Order deleted successfully | order Id={}", id);
    }

    @Override
    @Transactional
    public void deleteOrders(String orderId) {
        log.info("Deleting orders | orderID={}", orderId);
        orderDao.deleteOrderIdOrders(orderId);
        log.info("Orders deleted successfully | orderID={}", orderId);
    }

    @Override
    public PagedResponse<OrderResponse> getAllOrders(int page, int size, boolean isNewest) {

        Page<@NonNull Order> orders = orderDao.findAll(page, size, isNewest);

        return OrderService.buildPagedResponse(orders, OrderMapper::toOrderResponse);

    }


    @Override
    @Transactional
    public OrderResponse updateOrderStatus(String sellerId, Long id, OrderStatus newStatus) {

        log.info("Starting order status update | sellerId={} | order Id={} | newStatus={}",
                sellerId, id, newStatus);

        log.debug("Fetching order from database | order Id={}", id);
        Order existingOrder = orderDao.findById(id).orElseThrow(
                () -> {
                    log.error("Order not found | order Id={}", id);
                    return new ResourceNotFoundException("Order not found with id: " + id);
                }
        );
        log.debug("Order found | orderId={} | currentStatus={} | orderSellerId={}",
                id, existingOrder.getOrderStatus(), existingOrder.getSellerId());

        // checking only seller can update his own orders
        if (!existingOrder.getSellerId().equals(sellerId)) {
            log.warn("Unauthorized status update attempt | sellerId={} | orderSellerId={} | order id={}",
                    sellerId, existingOrder.getSellerId(), id);
            throw new UnauthorizedException("Seller " + sellerId + " is not authorized to update order " + id);
        }

        // no need to changes if same status request
        if (existingOrder.getOrderStatus().equals(newStatus)) {
            log.info("Order status unchanged | order Id={} | status={}", id, newStatus);
            return OrderMapper.toOrderResponse(existingOrder);
        }

        if (OrderService.isNotUpdateAbleStatus(existingOrder)) {
            log.info("Order status is not in updatable condition status: {}", existingOrder.getOrderStatus());
            throw new BusinessException("Order status is not updatable");
        }

        OrderStatus oldStatus = existingOrder.getOrderStatus();
        existingOrder.setOrderStatus(newStatus);

        log.info("Updating order status | order Id={} | oldStatus={} | newStatus={}",
                id, oldStatus, newStatus);

        log.debug("Saving updated order | orderId={}", id);
        Order savedOrder = orderDao.save(existingOrder);
        log.info("Order status updated successfully | order Id={} | oldStatus={} | newStatus={}",
                id, oldStatus, newStatus);

        return OrderMapper.toOrderResponse(savedOrder);

    }

    // for kafka
    @Override
    @Transactional
    public void updateOrderAndPaymentStatus(String orderId, OrderStatus orderStatus, PaymentStatus paymentStatus, String email) {
        log.info("Bulk updating order status | orderID={} | orderStatus={} | paymentStatus={}",
                orderId, orderStatus, paymentStatus);

        if (orderId == null || orderId.isEmpty()) {
            throw new ValidationException("Order is null or empty");
        }
        if (orderStatus == null) {
            throw new ValidationException("Order status is null");
        }
        if (paymentStatus == null) {
            throw new ValidationException("Payment status is null");
        }
        if (email == null || email.isEmpty()) {
            throw new ValidationException("Email is null or empty");
        }

        int updatedCount = orderDao.updateOrderAndPaymentStatus(orderId, orderStatus, paymentStatus);

        if (updatedCount == 0) {
            log.warn("No orders updated | orderID={}", orderId);
            throw new ResourceNotFoundException("Order not found with id: " + orderId);
        }
        log.info("Bulk update completed | orderID={} | updatedCount={} | orderStatus={} | paymentStatus={}",
                orderId, updatedCount, orderStatus, paymentStatus);
        log.info("Publish report and update stock");
        List<Order> orders = orderDao.findByOrderId(orderId);
        if (paymentStatus == PaymentStatus.SUCCESS) {
            publishWhenPaymentSuccess(orders);
        }

        sendOrderNotification(orderId, email, orderStatus,paymentStatus);
    }


    @Transactional
    @Override
    public List<OrderResponse> cancelOrder(String orderId, String userId, String email) {
        log.info("Attempting to cancel order | orderID={} | userId={}", orderId, userId);

        // Check if order exists
        if (!orderDao.existByOrderId(orderId)) {
            log.error("Order not found | orderID={}", orderId);
            throw new ResourceNotFoundException("Order not found: " + orderId);
        }

        // Check if order is older than 1 day
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        boolean isOrderOlderThanOneDay = orderDao.existByOrderIdAndCreatedAtBefore(orderId, oneDayAgo);

        if (isOrderOlderThanOneDay) {
            log.warn("Order cancellation rejected - too old | orderID={} | cutoffTime={}", orderId, oneDayAgo);
            throw new BusinessException("Order cannot be cancelled after 24 hours");
        }

        // Check if order belongs to user
        List<Order> userOrders = orderDao.findByOrderIdAndUserId(orderId, userId);

        if (userOrders.isEmpty()) {
            log.warn("Unauthorized cancellation attempt | orderID={} | userId={}", orderId, userId);
            throw new ResourceNotFoundException("No order available for this ORDER ID: " + orderId);
        }

        boolean allOrdersBelongToUser = userOrders.stream()
                .allMatch(order -> userId.equals(order.getUserId()));

        if (!allOrdersBelongToUser) {
            log.warn("Unauthorized cancellation attempt | orderID={} | userId={}", orderId, userId);
            throw new UnauthorizedException("You are not authorized to cancel this order");
        }

        List<Order> orders = cancelOrders(userOrders);
        log.info("Order cancelled successfully | orderID={} | userId={}", orderId, userId);

        // Publish events
        publishOrderCancelRelatedEvents(orders, orderId, email);

        return orders.stream().map(OrderMapper::toOrderResponse).toList();
    }

    private List<Order> cancelOrders(List<Order> orders) {
        String orderId = orders.get(0).getOrderId();

        // Check if ANY order is in non-cancellable status
        boolean hasNonCancellableOrders = orders.stream()
                .anyMatch(OrderService::isNonCancellableStatus);

        if (hasNonCancellableOrders) {
            List<Order> nonCancellableOrders = orders.stream()
                    .filter(OrderService::isNonCancellableStatus)
                    .toList();

            log.error("Cannot cancel order | orderID={} | reason=non_cancellable_orders_found | count={}",
                    orderId, nonCancellableOrders.size());

            nonCancellableOrders.forEach(order ->
                    log.debug("Non-cancellable order | sellerId={} | status={}",
                            order.getSellerId(), order.getOrderStatus()));

            throw new BusinessException("Cannot cancel order because some items have already been shipped/delivered");
        }

        // Cancel all orders
        orders.forEach(order -> {
            order.setOrderStatus(OrderStatus.CANCELLED);
            order.setPaymentStatus(PaymentStatus.REFUND_REQUEST);
            log.debug("Cancelling order | sellerId={}", order.getSellerId());
        });

        List<Order> savedAll = orderDao.saveAll(orders);
        log.info("All orders cancelled successfully | orderID={} | cancelledCount={}",
                orderId, orders.size());

        return savedAll;
    }


    @Async
    protected void publishOrderCancelRelatedEvents(List<Order> orders, String orderId, String email) {
        prepareAndPublishSellerReportEvents(orders);
        prepareAndPublishProductUpdateStockEvents(orders);
        sendOrderNotification(orderId, email, OrderStatus.CANCELLED,PaymentStatus.REFUND_REQUEST);
    }

    @Async
    protected void publishWhenPaymentSuccess(List<Order> orders) {
        prepareAndPublishSellerReportPaymentSuccessEvent(orders);
        prepareAndPublishProductUpdateStockEvents(orders);
    }

    private void prepareAndPublishSellerReportPaymentSuccessEvent(List<Order> orders) {
        orders.forEach(order -> {
            SellerReportCreateEvent event = SellerReportCreateEvent.builder()
                    .sellerId(order.getSellerId())
                    .totalOrders(1)
                    .totalEarnings((long) order.getTotalSellingPrice())
                    .totalSales(Long.valueOf(order.getTotalSellingPrice()))
                    .totalTransactions(1)
                    .netEarnings(Long.valueOf(order.getTotalSellingPrice()))
                    .build();
            kafkaPublisherService.publishSellerReportEvent(event);
        });
    }

    private void prepareAndPublishSellerReportEvents(List<Order> orders) {
        orders.forEach(order -> {
            SellerReportCreateEvent event = SellerReportCreateEvent.builder()
                    .cancelOrders(1)
                    .sellerId(order.getSellerId())
                    .totalRefunds((long) order.getTotalSellingPrice())
                    .build();

            kafkaPublisherService.publishSellerReportEvent(event);
        });
    }

    private void prepareAndPublishProductUpdateStockEvents(List<Order> orders) {
        orders.forEach(order -> order.getOrderItems().forEach(orderItem -> {
            ProductUpdateStockEvent event = ProductUpdateStockEvent.builder()
                    .productId(orderItem.getProductId())
                    .quantity(orderItem.getQuantity())
                    .build();
            kafkaPublisherService.publishUpdateProductStockEvent(event);
        }));
    }

    private void sendOrderNotification(String orderId, String email, OrderStatus orderStatus,PaymentStatus paymentStatus) {

        String body = EmailSendingTemplate.sendEmailForOrderStatus(email, orderId, orderStatus.name(), email,paymentStatus.name());
        SendNotificationEvent event = SendNotificationEvent.builder()
                .to(email)
                .subject("Order Details & Info")
                .body(body)
                .eventType("Order status: " + orderStatus)
                .build();

        kafkaPublisherService.publishSendNotificationEvent(event);

    }

}

