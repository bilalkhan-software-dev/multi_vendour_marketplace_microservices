package com.vendor_marketplace.order_service.dao.implementation;

import com.vendor_marketplace.order_service.dao.interfaces.ShippingAddressDao;
import com.vendor_marketplace.order_service.dao.repository.ShippingAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShippingAddressDaoImpl implements ShippingAddressDao {

    private final ShippingAddressRepository shippingAddressRepository;

}
