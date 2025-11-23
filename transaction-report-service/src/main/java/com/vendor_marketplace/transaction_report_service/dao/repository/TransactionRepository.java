package com.vendor_marketplace.transaction_report_service.dao.repository;

import com.vendor_marketplace.transaction_report_service.models.entity.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    List<Transaction> findBySellerId(String sellerId, Pageable pageable);

    List<Transaction> findByOrderId(String orderId, Sort sort);


    Optional<Transaction> findByOrderId(String orderId);
    Optional<Transaction> findBySellerId(String sellerId);

    void deleteAllBySellerId(String sellerId);

    void deleteAllByOrderId(String orderId);
}
