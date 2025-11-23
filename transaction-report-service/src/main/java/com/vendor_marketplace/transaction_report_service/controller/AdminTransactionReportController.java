package com.vendor_marketplace.transaction_report_service.controller;

import com.vendor_marketplace.transaction_report_service.handler.GenericResponseHandler;
import com.vendor_marketplace.transaction_report_service.models.dto.response.SellerReportResponse;
import com.vendor_marketplace.transaction_report_service.models.dto.response.TransactionResponse;
import com.vendor_marketplace.transaction_report_service.services.SellerReportService;
import com.vendor_marketplace.transaction_report_service.services.TransactionService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/admin/tr")
public class AdminTransactionReportController {


    private final TransactionService transactionService;
    private final SellerReportService reportService;
    private final GenericResponseHandler response;

    @GetMapping("/order")
    ResponseEntity<?> getTransactionOfTheOrder(@NotNull(message = "Order id is required") @RequestParam String orderId) {
        List<TransactionResponse> transactions = transactionService.getAllTransactionsOfTheOrder(orderId);

        return response.createBuildResponse("Transactions retrieved successfully", transactions, HttpStatus.OK);

    }

    @GetMapping("/transactions")
    ResponseEntity<?> getAllTransactions(
            @RequestParam(required = false, defaultValue = "0") int pageNo,
            @RequestParam(required = false, defaultValue = "15") int pageSize
    ) {
        List<TransactionResponse> transactions = transactionService.getAllTransactions(pageNo, pageSize);

        return response.createBuildResponse("Transactions retrieved successfully", transactions, HttpStatus.OK);

    }

    @DeleteMapping("/{id}/transaction")
    ResponseEntity<?> deleteTransactionById(@NotNull(message = "Id is required") @PathVariable Long id) {

        transactionService.deleteTransactionById(id);

        return response.createBuildResponseMessage("Transaction deleted successfully", HttpStatus.OK);

    }

    @DeleteMapping("/{sellerId}/seller/transaction")
    ResponseEntity<?> deleteTransactionOfTheSeller(@NotNull(message = "Seller Id is required") @PathVariable String sellerId) {

        transactionService.deleteTransactionOfTheSeller(sellerId);

        return response.createBuildResponseMessage("Seller: " + sellerId + " transaction deleted successfully", HttpStatus.OK);

    }

    @DeleteMapping("/{orderId}/order")
    ResponseEntity<?> deleteTransactionOfTheOrder(@NotNull(message = "Order Id is required") @PathVariable String orderId) {

        transactionService.deleteTransactionOfTheOrder(orderId);

        return response.createBuildResponseMessage("Order: " + orderId + " transaction deleted successfully", HttpStatus.OK);

    }

    @GetMapping("/reports")
    ResponseEntity<?> getAllReports(
            @RequestParam(required = false, defaultValue = "0") int pageNo,
            @RequestParam(required = false, defaultValue = "15") int pageSize
    ) {
        List<SellerReportResponse> transactions = reportService.getAllReport(pageNo, pageSize);

        return response.createBuildResponse("Seller Reports retrieved successfully", transactions, HttpStatus.OK);

    }


    @DeleteMapping("/{id}/report")
    ResponseEntity<?> deleteSellerReportById(
            @NotNull(message = "Id is required") @PathVariable Long id) {

        reportService.deleteSellerReportById(id);

        return response.createBuildResponseMessage("Report deleted successfully", HttpStatus.OK);

    }

    @DeleteMapping("/{sellerId}/seller/report")
    ResponseEntity<?> deleteSellerReportOfTheSeller(
            @NotNull(message = "Seller id is required") @PathVariable String sellerId) {

        reportService.deleteSellerReports(sellerId);

        return response.createBuildResponseMessage("Seller: " + sellerId + " reports deleted successfully", HttpStatus.OK);

    }


}
