package com.vendor_marketplace.seller_service.dao.interfaces;

import com.vendor_marketplace.seller_service.models.entity.Seller;
import com.vendor_marketplace.seller_service.models.entity.SellerAddress;
import com.vendor_marketplace.seller_service.models.entity.enums.AccountStatus;

import java.util.List;
import java.util.Optional;

public interface SellerDao {

    Optional<Seller> findByEmail(String email);

    Optional<Seller> findById(Long id);

    Boolean checkKeycloakOrEmailExist(String keycloak, String email);

    Boolean existsById(Long id);
    Seller saveUser(Seller user);
    List<Seller> getAllUsers();

    void deleteSellerById(Long sellerId);

    AccountStatus getSellerAccountStatus(Long sellerId);
}
