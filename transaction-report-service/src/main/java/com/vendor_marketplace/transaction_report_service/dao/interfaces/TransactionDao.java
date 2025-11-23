package com.vendor_marketplace.transaction_report_service.dao.interfaces;

import com.vendor_marketplace.transaction_report_service.models.entity.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionDao {

    void save(Transaction transaction);

    List<Transaction> findAllTransactionsBySeller(String seller, int page, int pageSize);


    List<Transaction> findAllTransactionsByOrderId(String orderId);

    Optional<Transaction> findTransactionById(Long id);

    List<Transaction> findAllTransactions(int page, int pageSize);

    void deleteTransactionById(Long id);

    void deleteAllTransactionOfTheSeller(String sellerId);

    void deleteAllTransactionOfTheOrder(String orderId);
}
