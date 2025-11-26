package com.vendor_marketplace.common.dto.event;


import com.vendor_marketplace.common.dto.enums.OrderStatus;
import com.vendor_marketplace.common.dto.enums.PaymentStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentCancelOrFailEvent {

    private String orderId;

    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.PAYMENT_FAILED;

    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.FAILED;

    private String email;


}
