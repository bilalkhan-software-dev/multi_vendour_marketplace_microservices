package com.vendor_marketplace.seller_service.dao.interfaces;

import com.vendor_marketplace.seller_service.models.entity.Seller;
import com.vendor_marketplace.seller_service.models.entity.SellerAddress;

import java.util.List;
import java.util.Optional;

public interface SellerDao {

    Optional<Seller> findByEmail(String email);

    Boolean checkKeycloakOrEmailExist(String keycloak, String email);

    Boolean existsById(Long id);
    Seller saveUser(Seller user);
    Optional<Seller> getSellerById(Long id);
    List<Seller> getAllUsers();

    Optional<SellerAddress> getSellerAddress(Long sellerId);
}
