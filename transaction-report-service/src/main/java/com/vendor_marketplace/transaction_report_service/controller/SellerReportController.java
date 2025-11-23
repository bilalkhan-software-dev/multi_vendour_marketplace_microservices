package com.vendor_marketplace.transaction_report_service.controller;

import com.vendor_marketplace.transaction_report_service.handler.GenericResponseHandler;
import com.vendor_marketplace.transaction_report_service.models.dto.response.SellerReportResponse;
import com.vendor_marketplace.transaction_report_service.services.SellerReportService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/reports")
public class SellerReportController {

    private final SellerReportService reportService;
    private final GenericResponseHandler response;

    @GetMapping("")
    ResponseEntity<?> getAllReportsOfTheSeller(
            @NotNull(message = "Seller id is required") @RequestParam String sellerId,
            @RequestParam(required = false, defaultValue = "0") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        List<SellerReportResponse> transactions = reportService.getReportOfTheSeller(sellerId, pageNo, pageSize);

        return response.createBuildResponse("Seller Reports retrieved successfully", transactions, HttpStatus.OK);

    }


    @GetMapping("/{id}")
    ResponseEntity<?> getReportsById(
            @NotNull(message = "Seller id is required") @PathVariable Long id
    ) {
        SellerReportResponse transactions = reportService.getReportById(id);

        return response.createBuildResponse("Report details retrieved successfully", transactions, HttpStatus.OK);

    }


}
