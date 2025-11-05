package com.vendor_marketplace.seller_service.dao.implementation;

import com.vendor_marketplace.seller_service.dao.interfaces.SellerDao;
import com.vendor_marketplace.seller_service.dao.repository.SellerAddressRepository;
import com.vendor_marketplace.seller_service.dao.repository.SellerRepository;
import com.vendor_marketplace.seller_service.models.entity.Seller;
import com.vendor_marketplace.seller_service.models.entity.SellerAddress;
import com.vendor_marketplace.seller_service.models.entity.enums.AccountStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class SellerDaoImpl implements SellerDao {

    private final SellerRepository sellerRepository;

    @Override
    public Optional<Seller> findByEmail(String email) {

        return sellerRepository.findByEmail(email);
    }

    @Override
    public Optional<Seller> findById(Long id) {
        return sellerRepository.findById(id);
    }



    @Override
    public Boolean checkKeycloakOrEmailExist(String keycloak, String email) {
        return sellerRepository.existsByKeyCloakIdOrEmail(keycloak, email);
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
    public List<Seller> getAllUsers() {
        return sellerRepository.findAll();
    }

    @Override
    public void deleteSellerById(Long sellerId){
        sellerRepository.deleteById(sellerId);
    }


    @Override
    public AccountStatus getSellerAccountStatus(Long sellerId) {
        return sellerRepository.getSellerAccountStatus(sellerId);
    }
}
