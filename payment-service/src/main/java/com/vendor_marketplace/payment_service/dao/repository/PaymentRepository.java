package com.vendor_marketplace.payment_service.dao.repository;

import com.vendor_marketplace.payment_service.models.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
