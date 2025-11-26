package com.vendor_marketplace.payment_service.kafka.publisher;

import com.vendor_marketplace.common.dto.event.*;

public interface KafkaEventPublisher {

    void publishTransactionEvent(TransactionCreateEvent event);

    void publishSellerReportEvent(SellerReportCreateEvent event);

    void publishPaymentSuccessProductUpdateStockEvent(ProductUpdateStockEvent event);

    void publishPaymentSuccessEvent(PaymentSuccessEvent event);

    void publishPaymentCancelFailEvent(PaymentCancelOrFailEvent event);
}
