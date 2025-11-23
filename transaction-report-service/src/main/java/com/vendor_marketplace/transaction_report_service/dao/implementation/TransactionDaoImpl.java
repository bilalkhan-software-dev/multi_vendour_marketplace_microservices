package com.vendor_marketplace.transaction_report_service.dao.implementation;

import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.transaction_report_service.dao.interfaces.TransactionDao;
import com.vendor_marketplace.transaction_report_service.dao.repository.TransactionRepository;
import com.vendor_marketplace.transaction_report_service.models.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class TransactionDaoImpl implements TransactionDao {

    private final TransactionRepository transactionRepository;

    @Override
    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findAllTransactionsBySeller(String sellerId, int page, int pageSize) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return transactionRepository.findBySellerId(sellerId, pageable);
    }

    @Override
    public List<Transaction> findAllTransactionsByOrderId(String orderId) {

        Sort sort = Sort.by("createdAt").descending();
        return transactionRepository.findByOrderId(orderId, sort);
    }

    @Override
    public Optional<Transaction> findTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public List<Transaction> findAllTransactions(int page, int pageSize) {

        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        return transactionRepository.findAll(pageable).getContent();

    }

    @Override
    public void deleteTransactionById(Long id) {

        transactionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Transaction not found")
        );

        transactionRepository.deleteById(id);
    }

    @Override
    public void deleteAllTransactionOfTheSeller(String sellerId) {

        transactionRepository.findBySellerId(sellerId).orElseThrow(
                () -> new ResourceNotFoundException("Transaction not found with your requested seller id")
        );

        transactionRepository.deleteAllBySellerId(sellerId);
    }

    @Override
    public void deleteAllTransactionOfTheOrder(String orderId) {

        transactionRepository.findByOrderId(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Transaction not found with your request order id")
        );

        transactionRepository.deleteAllByOrderId(orderId);
    }


}
