package com.vendor_marketplace.user_service.dao.repository;

import com.vendor_marketplace.user_service.models.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Long>{


    List<Address> findByUserId(Long userId);
}
