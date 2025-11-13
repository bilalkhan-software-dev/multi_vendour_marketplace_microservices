package com.vendor_marketplace.auth_service.dao.interfaces;

import com.vendor_marketplace.auth_service.models.entity.AuthUser;
import com.vendor_marketplace.common.dto.enums.AccountStatus;

import java.util.List;
import java.util.Optional;

public interface AuthUserDao {
    Optional<AuthUser> findByEmail(String email);

    Optional<AuthUser> findById(String id);

    boolean existsByEmail(String email);

    boolean existsBySalesTaxRegistrationNumber(String strn);

    boolean existsById(String id);

    AuthUser save(AuthUser authUser);

    void deleteAuthUser(String id);

    List<AuthUser> findAll(int size, int page, String direction);

    AccountStatus getAccountStatus(String id);

}
