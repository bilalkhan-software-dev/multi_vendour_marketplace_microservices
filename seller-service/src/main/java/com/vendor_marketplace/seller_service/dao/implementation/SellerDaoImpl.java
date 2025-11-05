package com.vendor_marketplace.seller_service.dao.implementation;

import com.vendor_marketplace.seller_service.dao.interfaces.SellerDao;
import com.vendor_marketplace.seller_service.dao.repository.SellerAddressRepository;
import com.vendor_marketplace.seller_service.dao.repository.SellerRepository;
import com.vendor_marketplace.seller_service.models.entity.Seller;
import com.vendor_marketplace.seller_service.models.entity.SellerAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
class SellerDaoImpl implements SellerDao {

    private final SellerRepository sellerRepository;
    private final SellerAddressRepository sellerAddressRepository;

    @Override
    public Optional<Seller> findByEmail(String email) {
        return sellerRepository.findByEmail(email);
    }



    @Override
    public Boolean checkKeycloakOrEmailExist(String keycloak, String email) {
        return sellerRepository.existsByKeycloakIdOrEmail(keycloak, email);
    }

    @Override
    public Boolean existsById(Long id) {
        return sellerRepository.existsById(id);
    }

    @Override
    public Seller saveUser(Seller user) {
        return sellerRepository.save(user);
    }

    @Override
    public Optional<Seller> getSellerById(Long id) {
        return sellerRepository.findById(id);
    }

    @Override
    public List<Seller> getAllUsers() {
        return sellerRepository.findAll();
    }

    @Override
    public Optional<SellerAddress> getSellerAddress(Long sellerId) {
        return sellerAddressRepository.findById(sellerId);
    }
}
