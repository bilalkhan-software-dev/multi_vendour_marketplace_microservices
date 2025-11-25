package com.vendor_marketplace.order_service.models.entity;

import com.vendor_marketplace.common.dto.enums.OrderStatus;
import com.vendor_marketplace.common.dto.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Order extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 50)
    private String orderId;

    @Column(nullable = false)
    private String userId; // customer

    private String  cartId;

    @Column(nullable = false)
    private String sellerId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

//    @ManyToOne
//    private ShippingAddress shippingAddress;


    @Column(nullable = false)
    private Long addressId;

    private double totalMrpPrice;
    private Integer totalSellingPrice;
    private Integer totalItems;
    private Integer totalDiscount;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_payment_status")
    private PaymentStatus paymentStatus;

    @Builder.Default
    private LocalDateTime deliveryDate = LocalDateTime.now().plusDays(7);

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

}
