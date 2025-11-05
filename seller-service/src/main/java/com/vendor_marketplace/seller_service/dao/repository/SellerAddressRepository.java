package com.vendor_marketplace.seller_service.dao.repository;

import com.vendor_marketplace.seller_service.models.entity.SellerAddress;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SellerAddressRepository extends JpaRepository<SellerAddress,Long>{


    SellerAddress findBySellerId(Long sellerId);

}
