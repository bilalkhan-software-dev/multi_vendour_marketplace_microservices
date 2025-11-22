package com.vendor_marketplace.cart_service.services.Impl;

import com.vendor_marketplace.cart_service.dao.interfaces.CartDao;
import com.vendor_marketplace.cart_service.dao.interfaces.CartItemDao;
import com.vendor_marketplace.cart_service.mapper.CartMapper;
import com.vendor_marketplace.cart_service.models.dto.request.UpdateCartItemRequest;
import com.vendor_marketplace.cart_service.models.entity.Cart;
import com.vendor_marketplace.cart_service.models.entity.CartItem;
import com.vendor_marketplace.cart_service.services.CartItemService;
import com.vendor_marketplace.cart_service.utils.CartUtils;
import com.vendor_marketplace.common.dto.response.CartResponse;
import com.vendor_marketplace.common.dto.response.ProductResponse;
import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartItemServiceImpl implements CartItemService {

    private final CartItemDao cartItemDao;
    private final CartDao cartDao;
    private final CartUtils utils;

    @Override
    @Transactional
    public CartResponse.CartItemResponse updateCartItem(String userId, Long cartItemId, UpdateCartItemRequest request) {
        log.info("Updating cart item: {} for user: {}", cartItemId, userId);

        CartItem cartItem = cartItemDao.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        //  Validate ownership
        validateCartItemOwnership(cartItem, userId);

        String productId = cartItem.getProductId();
        ProductResponse productResponse = utils.validateAndGetProductDetails(productId, userId);

        //  Update cart item with validation
        utils.updateExistingCartItem(cartItem, request.getQuantity(), productResponse);

        //  Update cart totals
        Cart cart = cartDao.findCartById(cartItem.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        utils.updateCartTotals(cart);
        cartDao.save(cart);

        CartItem saved = cartItemDao.saveCartItem(cartItem);
        log.info("Successfully updated cart item: {} for user: {}", cartItemId, userId);

        return CartMapper.toCartItemResponse(saved);
    }

    @Override
    @Transactional
    public void removeCartItem(String userId, Long cartItemId) {
        log.info("Removing cart item: {} for user: {}", cartItemId, userId);

        CartItem cartItem = cartItemDao.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        //  Validate ownership
        validateCartItemOwnership(cartItem, userId);

        String cartId = cartItem.getCartId();
        Cart cart = cartDao.findCartById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        //  Remove from cart and update totals
        boolean removed = cart.getCartItems().removeIf(item -> item.getId().equals(cartItemId));
        if (removed) {
            utils.updateCartTotals(cart);
            cartDao.save(cart);
            cartItemDao.deleteItemFromCart(cartItem);
            log.info("Successfully removed cart item: {} for user: {}", cartItemId, userId);
        } else {
            log.warn("Cart item {} not found in cart {}", cartItemId, cartId);
            throw new ResourceNotFoundException("Cart item not found in cart");
        }
    }

    @Override
    public CartResponse.CartItemResponse findCartItemById(Long cartItemId) {
        log.debug("Finding cart item by id: {}", cartItemId);

        CartItem cartItem = cartItemDao.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        return CartMapper.toCartItemResponse(cartItem);
    }

    @Override
    public List<CartResponse.CartItemResponse> getCartItemsByUserOrCartId(String userId, String cartId) {
        log.debug("Finding cart items for user: {} or cart: {}", userId, cartId);

        List<CartItem> items = cartItemDao.findAllItemByCartOrUserId(cartId, userId);

        if (items.isEmpty()) {
            log.info("No cart items found for user: {} or cart: {}", userId, cartId);
        }

        return items.stream()
                .map(CartMapper::toCartItemResponse)
                .toList();
    }

    //  Security validation method
    private void validateCartItemOwnership(CartItem cartItem, String userId) {
        if (!cartItem.getUserId().equals(userId)) {
            log.warn("User {} attempted to access cart item {} belonging to user {}",
                    userId, cartItem.getId(), cartItem.getUserId());
            throw new UnauthorizedException("Cart item does not belong to the current user");
        }
    }

    @Transactional
    @Override
    public void clearUserCart(String userId) {
        log.info("Clearing all cart items for user: {}", userId);

        Cart cart = cartDao.findCartByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: " + userId));

        // Remove all cart items
        cart.getCartItems().clear();
        utils.setCartTotalsToZero(cart);
        cartDao.save(cart);

        log.info("Successfully cleared cart for user: {}", userId);
    }
}