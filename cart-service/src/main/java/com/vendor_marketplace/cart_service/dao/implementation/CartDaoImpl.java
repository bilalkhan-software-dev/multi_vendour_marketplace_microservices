package com.vendor_marketplace.cart_service.dao.implementation;

import com.vendor_marketplace.cart_service.dao.interfaces.CartDao;
import com.vendor_marketplace.cart_service.dao.repository.CartRepository;
import com.vendor_marketplace.cart_service.models.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartDaoImpl implements CartDao {

    private final CartRepository cartRepository;



    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Optional<Cart> findCartByUserId(String userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public Optional<Cart> findCartById(String cartId) {
        return cartRepository.findById(cartId);
    }





}
