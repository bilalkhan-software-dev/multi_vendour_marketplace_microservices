package com.vendor_marketplace.transaction_report_service.dao.repository;

import com.vendor_marketplace.transaction_report_service.models.entity.SellerReport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerReportRepository extends JpaRepository<SellerReport,Long> {
    Optional<SellerReport> findBySellerId(String sellerId);

    List<SellerReport> findAllBySellerId(String sellerId, Pageable pageable);

    boolean existsBySellerId(String sellerId);

    void deleteAllBySellerId(String sellerId);
}
