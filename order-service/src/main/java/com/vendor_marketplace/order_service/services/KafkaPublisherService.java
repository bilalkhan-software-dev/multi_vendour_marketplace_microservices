package com.vendor_marketplace.order_service.services;

import com.vendor_marketplace.common.dto.event.OrderCreatedEvent;
import com.vendor_marketplace.common.dto.event.OrderNotificationEvent;
import com.vendor_marketplace.common.dto.event.ProductUpdateStockEvent;
import com.vendor_marketplace.common.dto.event.SellerReportCreateEvent;

public interface KafkaPublisherService {

    void publishOrderCreatedEvent(OrderCreatedEvent event);

    void publishSellerReportEvent(SellerReportCreateEvent event); //  when order cancel by customer

    void publishOrderNotificationEvent(OrderNotificationEvent event);

    void publishProductUpdateStockEvent(ProductUpdateStockEvent event);


}
