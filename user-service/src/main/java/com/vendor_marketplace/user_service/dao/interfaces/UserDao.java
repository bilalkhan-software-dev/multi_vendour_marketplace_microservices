package com.vendor_marketplace.user_service.dao.interfaces;

import com.vendor_marketplace.user_service.models.entity.Address;
import com.vendor_marketplace.user_service.models.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> findByEmail(final String email);
    Optional<User> findById(final Long id);


    Boolean checkAuthIdOrEmailExist(String keycloak, String email);

    Boolean existsById(final Long id);


    Boolean existsByAuthId(String id);

    User saveUser(final User user);
    Optional<User> getUserById(final Long id);

    void deleteUserById(Long id);

    Page<User> getAllUsers(Integer pageNo);

    List<Address> getAddressesByUserId(final Long userId);
}
