package com.vendor_marketplace.user_service.dao.interfaces;

import com.vendor_marketplace.user_service.models.entity.Address;
import com.vendor_marketplace.user_service.models.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> findByEmail(final String email);
    Optional<User> findById(final Long id);

    Boolean checkKeycloakOrEmailExist(final String keycloak,final String email);

    Boolean existsById(final Long id);
    User saveUser(final User user);
    Optional<User> getUserById(final Long id);

    void deleteUserById(Long id);

    List<User> getAllUsers();

    List<Address> getAddressesByUserId(final Long userId);
}
