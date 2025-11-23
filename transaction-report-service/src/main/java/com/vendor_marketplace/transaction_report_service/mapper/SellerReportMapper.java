package com.vendor_marketplace.transaction_report_service.mapper;

import com.vendor_marketplace.transaction_report_service.models.dto.response.SellerReportResponse;
import com.vendor_marketplace.transaction_report_service.models.entity.SellerReport;

public class SellerReportMapper {
    public static SellerReportResponse toSellerReportResponse(SellerReport sellerReport) {
        if (sellerReport == null) {
            return null;
        }
        return SellerReportResponse.builder()
                .id(sellerReport.getId())
                .sellerId(sellerReport.getSellerId())
                .createdAt(sellerReport.getCreatedAt())
                .updatedAt(sellerReport.getUpdatedAt())
                .totalEarnings(sellerReport.getTotalEarnings())
                .totalSales(sellerReport.getTotalSales())
                .totalRefunds(sellerReport.getTotalRefunds())
                .totalTax(sellerReport.getTotalTax())
                .netEarnings(sellerReport.getNetEarnings())
                .totalOrders(sellerReport.getTotalOrders())
                .cancelOrders(sellerReport.getCancelOrders())
                .totalTransactions(sellerReport.getTotalTransactions())
                .build();
    }
}
