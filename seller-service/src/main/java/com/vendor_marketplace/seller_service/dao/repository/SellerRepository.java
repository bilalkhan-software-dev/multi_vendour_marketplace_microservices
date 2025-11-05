package com.vendor_marketplace.seller_service.dao.repository;

import com.vendor_marketplace.seller_service.models.entity.Seller;
import com.vendor_marketplace.seller_service.models.entity.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller,Long> {

    boolean existsByEmail(String email);

    boolean existsByKeyCloakIdOrEmail(String keyCloakId, String email);
    Optional<Seller> findById(Long id);
    Optional<Seller> findByEmail(String email);


    @Query("select s.accountStatus from Seller s where s.id = :id")
    AccountStatus getSellerAccountStatus(@Param("id") Long sellerId);



}
