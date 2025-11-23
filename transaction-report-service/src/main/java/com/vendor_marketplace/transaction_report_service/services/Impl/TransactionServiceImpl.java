package com.vendor_marketplace.transaction_report_service.services.Impl;

import com.vendor_marketplace.common.dto.event.TransactionCreateEvent;
import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.transaction_report_service.dao.interfaces.TransactionDao;
import com.vendor_marketplace.transaction_report_service.mapper.TransactionMapper;
import com.vendor_marketplace.transaction_report_service.models.dto.response.TransactionResponse;
import com.vendor_marketplace.transaction_report_service.models.entity.Transaction;
import com.vendor_marketplace.transaction_report_service.services.TransactionService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
class TransactionServiceImpl implements TransactionService {

    private final TransactionDao transactionDao;

    @Override
    public void savaTransaction(TransactionCreateEvent event) {

        validateEvent(event);

        Transaction transaction = Transaction.builder()
                .customerId(event.getCustomerId())
                .orderId(event.getOrderId())
                .sellerId(event.getSellerId())
                .build();
        transactionDao.save(transaction);

    }

    @Override
    public TransactionResponse getTransactionId(Long id) {

        Transaction transaction = transactionDao.findTransactionById(id).orElseThrow(
                () -> new ResourceNotFoundException("Transaction not found")
        );

        return TransactionMapper.toTransactionResponse(transaction);

    }


    @Override
    public List<TransactionResponse> getAllTransactions(int page, int size) {

        List<Transaction> transactions = transactionDao.findAllTransactions(page, size);

        return transactions.stream().map(TransactionMapper::toTransactionResponse).toList();
    }

    @Override
    public List<TransactionResponse> getAllTransactionsOfTheOrder(String orderId) {

        List<Transaction> transactions = transactionDao.findAllTransactionsByOrderId(orderId);

        return transactions.stream().map(TransactionMapper::toTransactionResponse).toList();
    }

    @Override
    public List<TransactionResponse> getAllTransactionsOfTheSeller(String sellerId, int page, int size) {

        List<Transaction> transactions = transactionDao.findAllTransactionsBySeller(sellerId, page, size);

        return transactions.stream().map(TransactionMapper::toTransactionResponse).toList();
    }

    @Override
    public void deleteTransactionById(Long id) {

        transactionDao.deleteTransactionById(id);

    }

    @Override
    public void deleteTransactionOfTheSeller(String sellerId) {

        transactionDao.deleteAllTransactionOfTheSeller(sellerId);

    }

    @Override
    public void deleteTransactionOfTheOrder(String orderId) {

        transactionDao.deleteAllTransactionOfTheOrder(orderId);
    }


    private void validateEvent(TransactionCreateEvent event) {
        log.info("Validating event {}", event);
        if (event.getCustomerId() == null || event.getOrderId() == null || event.getSellerId() == null) {
            log.info("Skipping transaction save event because seller id and seller id is invalid :: Event: {}", event);
            throw new ValidationException("Seller | Order | Customer id are missing");
        }
        log.info("Event Validated Successfully");
    }

}
