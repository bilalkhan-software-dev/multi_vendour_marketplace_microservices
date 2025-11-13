package com.vendor_marketplace.auth_service.dao.repository;

import com.vendor_marketplace.auth_service.models.entity.AuthUser;
import com.vendor_marketplace.common.dto.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository extends JpaRepository<AuthUser, UUID> {

    boolean existsByEmail(String email);

    Optional<AuthUser> findByEmail(String email);

    @Query("SELECT u.accountStatus FROM AuthUser u where u.id= :id ")
    AccountStatus getSellerAccountStatus(@Param("id") String id);

    boolean existsBySalesTaxRegistrationNumber(String salesTaxRegistrationNumber);

}