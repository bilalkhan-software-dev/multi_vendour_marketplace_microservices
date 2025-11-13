package com.vendor_marketplace.user_service.dao.repository;

import com.vendor_marketplace.user_service.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByAuthIdOrEmail(String keycloakId, String email);
    Optional<User> findByEmail(String email);


    Boolean existsByAuthId(String id);

}
