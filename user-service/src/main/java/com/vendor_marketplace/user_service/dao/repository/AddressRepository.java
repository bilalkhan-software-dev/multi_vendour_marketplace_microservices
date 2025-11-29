package com.vendor_marketplace.user_service.dao.repository;

import com.vendor_marketplace.user_service.models.dto.response.UserAddressResponse;
import com.vendor_marketplace.user_service.models.dto.response.UserResponse;
import com.vendor_marketplace.user_service.models.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {


    List<Address> findByUserId(Long userId);


    @Query("SELECT new com.vendor_marketplace.user_service.models.dto.response.UserAddressResponse(" +
            "a.id, a.name, a.locality, a.city, a.state, a.pinCode, a.mobile, a.address, a.user.authId, a.createdAt, a.updatedAt) " +
            "FROM Address a WHERE a.id = :id")
    Optional<UserAddressResponse> findUserAddressById(@Param("id") Long id);

}
