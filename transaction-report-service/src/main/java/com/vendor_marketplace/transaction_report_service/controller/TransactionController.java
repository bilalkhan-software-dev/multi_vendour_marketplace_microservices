package com.vendor_marketplace.transaction_report_service.controller;


import com.vendor_marketplace.transaction_report_service.handler.GenericResponseHandler;
import com.vendor_marketplace.transaction_report_service.models.dto.response.TransactionResponse;
import com.vendor_marketplace.transaction_report_service.services.TransactionService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final GenericResponseHandler response;


    @GetMapping("/{id}")
    ResponseEntity<?> getTransactionById(@PathVariable Long id) {

        TransactionResponse transaction = transactionService.getTransactionId(id);

        return response.createBuildResponse("Transaction detail retrieved successfully", transaction, HttpStatus.OK);

    }


    @GetMapping()
    ResponseEntity<?> getTransactionOfTheSeller(
            @NotNull(message = "Seller id is required") @RequestParam String sellerId,
            @RequestParam(required = false, defaultValue = "0") int pageNo,
            @RequestParam(required = false, defaultValue = "12") int pageSize
    ) {
        List<TransactionResponse> transactions = transactionService.getAllTransactionsOfTheSeller(sellerId, pageNo, pageSize);

        return response.createBuildResponse("Transactions retrieved successfully", transactions, HttpStatus.OK);

    }






}
