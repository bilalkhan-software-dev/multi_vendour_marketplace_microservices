package com.vendor_marketplace.seller_service.dao.interfaces;

import com.vendor_marketplace.seller_service.models.entity.Seller;
import com.vendor_marketplace.common.dto.enums.AccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SellerDao {

    Optional<Seller> findByEmail(String email);

    Optional<Seller> findById(Long id);


    Optional<Seller> findByAuthId(String id);

    Boolean checkAuthIdOrEmailExist(String keycloak, String email);

    Boolean existsById(Long id);


    Boolean existsByAuthId(String id);

    Boolean existsByStrn(String strn);

    Seller saveUser(Seller user);
    Page<Seller> getAllUsers(Pageable pageable);

    void deleteSellerById(Long sellerId);

}
