package com.vendor_marketplace.transaction_report_service.models.dto.response;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {

    private Long id;
    private String orderId;
    private String customerId;
    private String sellerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
