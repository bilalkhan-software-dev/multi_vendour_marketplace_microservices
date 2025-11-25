package com.vendor_marketplace.payment_service.services.Impl;

import com.vendor_marketplace.payment_service.config.StripeConfig;
import com.vendor_marketplace.payment_service.services.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {

    private final StripeConfig stripeConfig;


}
