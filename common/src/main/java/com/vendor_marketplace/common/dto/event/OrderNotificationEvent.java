package com.vendor_marketplace.common.dto.event;

import com.vendor_marketplace.common.dto.enums.OrderStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderNotificationEvent {

    private String to;
    private String orderId;
    private OrderStatus orderStatus;

}
