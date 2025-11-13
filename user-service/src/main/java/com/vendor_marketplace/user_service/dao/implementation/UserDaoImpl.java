package com.vendor_marketplace.user_service.dao.implementation;

import com.vendor_marketplace.user_service.dao.interfaces.UserDao;
import com.vendor_marketplace.user_service.dao.repository.AddressRepository;
import com.vendor_marketplace.user_service.dao.repository.UserRepository;
import com.vendor_marketplace.user_service.models.entity.Address;
import com.vendor_marketplace.user_service.models.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
class UserDaoImpl implements UserDao {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Boolean checkAuthIdOrEmailExist(String keycloak, String email) {
        return userRepository.existsByAuthIdOrEmail(keycloak,email);
    }

    @Override
    public Boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public Boolean existsByAuthId(String id) {
        return userRepository.existsByAuthId(id);
    }

    @Override
    public User saveUser(User user) {
       return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteUserById(Long id){
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public List<Address> getAddressesByUserId(Long userId) {
        return addressRepository.findByUserId(userId);
    }



}
