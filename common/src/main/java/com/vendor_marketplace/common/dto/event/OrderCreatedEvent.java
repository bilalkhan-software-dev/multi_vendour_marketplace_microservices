package com.vendor_marketplace.common.dto.event;

import com.vendor_marketplace.common.dto.enums.PaymentMethod;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderCreatedEvent {

    private List<String> sellerIds;
    private Integer totalAmount;
    private PaymentMethod paymentMethod;
    private String customerId;
    private String customerEmail;
    private String orderId;


}
