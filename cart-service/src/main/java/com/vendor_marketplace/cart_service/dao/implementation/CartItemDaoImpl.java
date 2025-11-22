package com.vendor_marketplace.cart_service.dao.implementation;

import com.vendor_marketplace.cart_service.dao.interfaces.CartItemDao;
import com.vendor_marketplace.cart_service.dao.repository.CartItemRepository;
import com.vendor_marketplace.cart_service.models.entity.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemDaoImpl implements CartItemDao {

    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem findByProductIdAndUserId(String productId, String userId) {

        return cartItemRepository.findByProductIdAndUserId(productId, userId);
    }

    @Override
    public CartItem saveCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    public Optional<CartItem> findById(Long id) {
        return cartItemRepository.findById(id);
    }

    @Override
    public List<CartItem> findAllItemByCartOrUserId(String cartId, String userId) {

        if (cartId == null || cartId.isEmpty()) {
            return cartItemRepository.findByUserId(userId);
        } else if (userId == null || userId.isEmpty()) {
            return cartItemRepository.findByCartId(cartId);
        } else {
            return cartItemRepository.findByCartIdOrUserId(cartId, userId);
        }
    }

    @Override
    public void deleteItemFromCart(CartItem cartItem) {
        cartItemRepository.deleteById(cartItem.getId());
    }


}
