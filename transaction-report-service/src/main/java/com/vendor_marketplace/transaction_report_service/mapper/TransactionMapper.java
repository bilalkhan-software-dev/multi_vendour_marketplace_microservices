package com.vendor_marketplace.transaction_report_service.mapper;

import com.vendor_marketplace.transaction_report_service.models.dto.response.TransactionResponse;
import com.vendor_marketplace.transaction_report_service.models.entity.Transaction;

public class TransactionMapper {
    public static TransactionResponse toTransactionResponse(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        return TransactionResponse.builder()
                .id(transaction.getId())
                .customerId(transaction.getCustomerId())
                .sellerId(transaction.getSellerId())
                .orderId(transaction.getOrderId())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }
}
