package com.vendor_marketplace.order_service.dao.repository;

import com.vendor_marketplace.order_service.models.entity.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress,Long> {


}
