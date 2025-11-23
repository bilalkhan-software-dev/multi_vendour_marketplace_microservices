package com.vendor_marketplace.transaction_report_service.dao.interfaces;

import com.vendor_marketplace.transaction_report_service.models.entity.SellerReport;

import java.util.List;
import java.util.Optional;

public interface SellerReportDao {

    SellerReport save(SellerReport sellerReport);

    Optional<SellerReport> findById(Long id);

    Optional<SellerReport> findBySellerId(String sellerId);


    List<SellerReport> findAllReportOfTheSeller(String sellerId, int page, int size);

    List<SellerReport> findAllReport(int page, int size);

    boolean existsBySellerId(String sellerId);

    void deleteSellerReportById(Long id);

    void deleteAllReportOfTheSeller(String sellerId);
}
