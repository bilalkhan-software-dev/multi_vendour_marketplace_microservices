package com.vendor_marketplace.payment_service.dao.interfaces;

import com.vendor_marketplace.payment_service.models.entity.Payment;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PaymentDao {

    Payment save(Payment payment);


    Optional<Payment> findById(Long id);

    Optional<Payment> findByOrderId(String orderId);

    Optional<Payment> findByPaymentSessionId(String paymentSessionId);

    boolean existById(Long id);

    boolean existByOrderId(String orderId);

    boolean existByPaymentSessionId(String paymentSessionId);

    void deleteById(Long id);

    void deleteByOrderId(String orderId);

    String findPaymentLinkOfTheOrder(String orderId);

    Page<Payment> findAll(int page, int size, boolean isNewest);
}
