package com.vendor_marketplace.order_service.kafka.publisher;

import com.vendor_marketplace.common.dto.event.OrderCreatedEvent;
import com.vendor_marketplace.common.dto.event.ProductUpdateStockEvent;
import com.vendor_marketplace.common.dto.event.SellerReportCreateEvent;
import com.vendor_marketplace.common.dto.event.SendNotificationEvent;

public interface KafkaPublisherService {

    void publishOrderCreatedEvent(OrderCreatedEvent event);

    void publishSellerReportEvent(SellerReportCreateEvent event); //  when order cancel by customer

    void publishSendNotificationEvent(SendNotificationEvent event);

    void publishUpdateProductStockEvent(ProductUpdateStockEvent event);
}
