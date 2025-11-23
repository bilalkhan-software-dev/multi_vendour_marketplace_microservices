package com.vendor_marketplace.transaction_report_service.services;

import com.vendor_marketplace.common.dto.event.TransactionCreateEvent;
import com.vendor_marketplace.transaction_report_service.models.dto.response.TransactionResponse;

import java.util.List;

public interface TransactionService {
    void savaTransaction(TransactionCreateEvent event);

    TransactionResponse getTransactionId(Long id);

    List<TransactionResponse> getAllTransactions(int page, int size);

    List<TransactionResponse> getAllTransactionsOfTheOrder(String orderId);

    List<TransactionResponse> getAllTransactionsOfTheSeller(String sellerId, int page, int size);

    void deleteTransactionById(Long id);

    void deleteTransactionOfTheSeller(String sellerId);

    void deleteTransactionOfTheOrder(String orderId);
}
