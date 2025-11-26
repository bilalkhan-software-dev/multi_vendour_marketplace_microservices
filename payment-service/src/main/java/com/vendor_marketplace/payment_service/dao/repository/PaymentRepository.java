package com.vendor_marketplace.payment_service.dao.repository;

import com.vendor_marketplace.payment_service.models.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(String orderId);
    Optional<Payment> findByPaymentSessionId(String paymentSessionId);

    boolean existsByOrderId(String orderId);
    boolean existsByPaymentSessionId(String paymentSessionId);

    void deleteByOrderId(String orderId);


    @Query("SELECT p.paymentLinkUrl FROM Payment p WHERE p.orderId = :orderId")
    String findPaymentLinkByOrderId(@Param("orderId")String orderId);

}
