package com.vendor_marketplace.seller_service.dao.repository;

import com.vendor_marketplace.seller_service.models.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Long> {

    boolean existsByEmail(String email);
    boolean existsByAuthIdOrEmail(String keyCloakId, String email);
    Optional<Seller> findById(Long id);
    Optional<Seller> findByEmail(String email);
    Boolean existsByAuthId(String id);
    Boolean existsBySTRN(String STRN);

}
