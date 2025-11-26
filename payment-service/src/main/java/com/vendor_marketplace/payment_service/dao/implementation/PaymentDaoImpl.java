package com.vendor_marketplace.payment_service.dao.implementation;

import com.vendor_marketplace.payment_service.dao.interfaces.PaymentDao;
import com.vendor_marketplace.payment_service.dao.repository.PaymentRepository;
import com.vendor_marketplace.payment_service.models.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
class PaymentDaoImpl implements PaymentDao {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    @Override
    public Optional<Payment> findByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    @Override
    public Optional<Payment> findByPaymentSessionId(String paymentSessionId) {
        return paymentRepository.findByPaymentSessionId(paymentSessionId);
    }


    @Override
    public boolean existById(Long id) {
        return paymentRepository.existsById(id);
    }

    @Override
    public boolean existByOrderId(String orderId) {
        return paymentRepository.existsByOrderId(orderId);
    }

    @Override
    public boolean existByPaymentSessionId(String paymentSessionId) {
        return paymentRepository.existsByPaymentSessionId(paymentSessionId);
    }

    @Override
    public void deleteById(Long id) {

        paymentRepository.deleteById(id);

    }

    @Override
    public void deleteByOrderId(String orderId) {

        paymentRepository.deleteByOrderId(orderId);

    }

    @Override
    public String findPaymentLinkOfTheOrder(String orderId) {
        return paymentRepository.findPaymentLinkByOrderId(orderId);
    }


    @Override
    public Page<Payment> findAll(int page, int size, boolean isNewest) {

        Sort sort = Sort.by(isNewest ? Sort.Direction.DESC : Sort.Direction.ASC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        return paymentRepository.findAll(pageable);
    }


}
