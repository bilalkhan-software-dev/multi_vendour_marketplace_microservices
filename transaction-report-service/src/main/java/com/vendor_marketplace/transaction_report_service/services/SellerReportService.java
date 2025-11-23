package com.vendor_marketplace.transaction_report_service.services;

import com.vendor_marketplace.common.dto.event.SellerReportCreateEvent;
import com.vendor_marketplace.transaction_report_service.models.dto.response.SellerReportResponse;

import java.util.List;

public interface SellerReportService {

    void saveReportWithMonthlyReset(SellerReportCreateEvent event);

    List<SellerReportResponse> getReportOfTheSeller(String sellerId, int page, int pageSize);

    List<SellerReportResponse> getAllReport(int page, int pageSize);

    SellerReportResponse getReportById(Long id);

    void deleteSellerReportById(Long id);

    void deleteSellerReports(String sellerId);

}
