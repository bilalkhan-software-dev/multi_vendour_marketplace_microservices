package com.vendor_marketplace.payment_service.dao.implementation;

import com.vendor_marketplace.payment_service.dao.interfaces.PaymentDao;
import com.vendor_marketplace.payment_service.dao.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PaymentDaoImpl implements PaymentDao {

    private final PaymentRepository paymentRepository;



}
