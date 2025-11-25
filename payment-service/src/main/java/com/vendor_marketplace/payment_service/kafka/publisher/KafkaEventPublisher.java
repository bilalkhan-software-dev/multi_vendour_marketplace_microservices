package com.vendor_marketplace.payment_service.kafka.publisher;

import com.vendor_marketplace.common.dto.event.ProductUpdateStockEvent;
import com.vendor_marketplace.common.dto.event.SellerReportCreateEvent;
import com.vendor_marketplace.common.dto.event.TransactionCreateEvent;

public interface KafkaEventPublisher {
    void publishTransactionEvent(TransactionCreateEvent event);

    void publishSellerReportEvent(SellerReportCreateEvent event);

    void publishPaymentSuccessProductUpdateStockEvent(ProductUpdateStockEvent event);
}
