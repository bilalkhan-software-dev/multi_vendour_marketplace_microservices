package com.vendor_marketplace.transaction_report_service.services.Impl;

import com.vendor_marketplace.common.dto.event.SellerReportCreateEvent;
import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.transaction_report_service.dao.interfaces.SellerReportDao;
import com.vendor_marketplace.transaction_report_service.mapper.SellerReportMapper;
import com.vendor_marketplace.transaction_report_service.models.dto.response.SellerReportResponse;
import com.vendor_marketplace.transaction_report_service.models.entity.SellerReport;
import com.vendor_marketplace.transaction_report_service.services.SellerReportService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SellerReportServiceImpl implements SellerReportService {

    private final SellerReportDao sellerReportDao;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss");

    @Override
    public void saveReportWithMonthlyReset(SellerReportCreateEvent event) {
        log.info("Processing seller report update for seller ID: {}", event.getSellerId());
        validateEvent(event);
        SellerReport existingReport = sellerReportDao.findBySellerId(event.getSellerId()).orElse(null);

        if (existingReport == null) {
            log.info("No existing report found for seller ID: {}. Creating new report.", event.getSellerId());
            createNewReport(event);
        } else if (existingReport.getCreatedAt() != null && isReportOlderThan30Days(existingReport.getCreatedAt())) {
            long daysOld = ChronoUnit.DAYS.between(existingReport.getCreatedAt(), LocalDateTime.now());
            log.info("Report for seller ID: {} is {} days old (created on {}). Creating new monthly report.",
                    event.getSellerId(), daysOld, formatDate(existingReport.getCreatedAt()));
            createNewReport(event);
        } else {
            LocalDateTime createdAt = existingReport.getCreatedAt();
            String createdAtFormatted = (createdAt != null) ? formatDate(createdAt) : "unknown date";

            log.debug("Updating existing report for seller ID: {} (created on {})",
                    event.getSellerId(), createdAtFormatted);
            updateExistingReport(existingReport, event);
        }
        log.info("Successfully processed seller report for seller ID: {}", event.getSellerId());
    }

    @Override
    public List<SellerReportResponse> getReportOfTheSeller(String sellerId, int page, int pageSize) {

        List<SellerReport> reports = sellerReportDao.findAllReportOfTheSeller(sellerId, page, pageSize);

        return reports.stream().map(SellerReportMapper::toSellerReportResponse).toList();

    }

    @Override
    public List<SellerReportResponse> getAllReport(int page, int pageSize) {
        List<SellerReport> reports = sellerReportDao.findAllReport(page, pageSize);

        return reports.stream().map(SellerReportMapper::toSellerReportResponse).toList();

    }

    @Override
    public SellerReportResponse getReportById(Long id) {
        return SellerReportMapper.toSellerReportResponse(sellerReportDao.findById(id)
                .orElseThrow
                        (() -> new ResourceNotFoundException("Report not found for id: " + id))
        );
    }

    @Override
    public void deleteSellerReportById(Long id) {

        sellerReportDao.deleteSellerReportById(id);

    }

    @Override
    public void deleteSellerReports(String sellerId) {
        sellerReportDao.deleteAllReportOfTheSeller(sellerId);
    }

    private void createNewReport(SellerReportCreateEvent event) {
        SellerReport newReport = SellerReport.builder()
                .sellerId(event.getSellerId())
                .cancelOrders(event.getCancelOrders())
                .totalEarnings(event.getTotalEarnings())
                .totalSales(event.getTotalSales())
                .totalRefunds(event.getTotalRefunds())
                .totalTax(event.getTotalTax())
                .netEarnings(event.getNetEarnings())
                .totalOrders(event.getTotalOrders())
                .totalTransactions(event.getTotalTransactions())
                .build();

        sellerReportDao.save(newReport);

        log.info("New seller report created successfully for seller ID: {}. " +
                        "Initial stats -> Orders: {}, Sales: {}, Earnings: Rs. {}",
                event.getSellerId(),
                event.getTotalOrders(),
                event.getTotalSales(),
                event.getTotalEarnings());
    }

    private void updateExistingReport(SellerReport existingReport, SellerReportCreateEvent event) {
        int oldOrders = existingReport.getTotalOrders();
        long oldSales = existingReport.getTotalSales();
        double oldEarnings = existingReport.getTotalEarnings();

        // Update the report
        existingReport.setCancelOrders(existingReport.getCancelOrders() + event.getCancelOrders());
        existingReport.setTotalEarnings(existingReport.getTotalEarnings() + event.getTotalEarnings());
        existingReport.setTotalSales(existingReport.getTotalSales() + event.getTotalSales());
        existingReport.setTotalRefunds(existingReport.getTotalRefunds() + event.getTotalRefunds());
        existingReport.setTotalTax(existingReport.getTotalTax() + event.getTotalTax());
        existingReport.setNetEarnings(existingReport.getNetEarnings() + event.getNetEarnings());
        existingReport.setTotalOrders(existingReport.getTotalOrders() + event.getTotalOrders());
        existingReport.setTotalTransactions(existingReport.getTotalTransactions() + event.getTotalTransactions());

        SellerReport updatedReport = sellerReportDao.save(existingReport);

        log.info("Seller report updated for seller ID: {}. " +
                        "Changes -> Orders: {} → {} (+{}), Sales: {} → {} (+{}), Earnings: Rs. {} → Rs. {} (+Rs. {})",
                event.getSellerId(),
                oldOrders, updatedReport.getTotalOrders(), event.getTotalOrders(),
                oldSales, updatedReport.getTotalSales(), event.getTotalSales(),
                oldEarnings,
                updatedReport.getTotalEarnings(),
                event.getTotalEarnings());

        log.debug("Detailed update for seller ID: {} - Canceled Orders: +{}, Refunds: +Rs. {}, Tax: +Rs. {}, Net Earnings: +Rs. {}",
                event.getSellerId(),
                event.getCancelOrders(),
                event.getTotalRefunds(),
                event.getTotalTax(),
                event.getNetEarnings());
    }

    private boolean isReportOlderThan30Days(LocalDateTime reportCreatedAt) {
        return reportCreatedAt.isBefore(LocalDateTime.now().minusDays(30));
    }

    private String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DATE_FORMATTER);
    }

    private void validateEvent(SellerReportCreateEvent event) {
        log.info("==== Validating Event ====");
        if (event.getSellerId() == null || event.getSellerId().isEmpty()) {
            log.error("Seller ID is null or empty from event {}", event);
            log.info("Skipping event for report created");
            throw new ValidationException("Seller ID is null or empty");
        }
        log.info("Event Validated Successfully sellerId: {}", event.getSellerId());
    }

}