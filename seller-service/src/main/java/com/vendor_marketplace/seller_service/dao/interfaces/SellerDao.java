package com.vendor_marketplace.seller_service.dao.interfaces;

import com.vendor_marketplace.seller_service.models.entity.Seller;
import com.vendor_marketplace.common.dto.enums.AccountStatus;

import java.util.List;
import java.util.Optional;

public interface SellerDao {

    Optional<Seller> findByEmail(String email);

    Optional<Seller> findById(Long id);


    Boolean checkAuthIdOrEmailExist(String keycloak, String email);

    Boolean existsById(Long id);


    Boolean existsByAuthId(String id);

    Boolean existsByStrn(String strn);

    Seller saveUser(Seller user);
    List<Seller> getAllUsers();

    void deleteSellerById(Long sellerId);

}
