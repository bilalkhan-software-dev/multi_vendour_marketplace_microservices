package com.vendor_marketplace.common.dto.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionCreateEvent {

    private String orderId;
    private String customerId;
    private String sellerId;

}
