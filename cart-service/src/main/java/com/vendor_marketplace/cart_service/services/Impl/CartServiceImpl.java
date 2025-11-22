package com.vendor_marketplace.cart_service.services.Impl;

import com.vendor_marketplace.cart_service.dao.interfaces.CartDao;
import com.vendor_marketplace.cart_service.dao.interfaces.CartItemDao;
import com.vendor_marketplace.cart_service.mapper.CartMapper;
import com.vendor_marketplace.cart_service.models.dto.request.AddToCartItemRequest;
import com.vendor_marketplace.cart_service.models.entity.Cart;
import com.vendor_marketplace.cart_service.models.entity.CartItem;
import com.vendor_marketplace.cart_service.services.CartService;
import com.vendor_marketplace.cart_service.utils.CartUtils;
import com.vendor_marketplace.common.dto.response.CartResponse;
import com.vendor_marketplace.common.dto.response.ProductResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartDao cartDao;
    private final CartItemDao cartItemDao;
    private final CartUtils utils;

    @Override
    @Transactional
    public CartResponse getUserCart(String userId) {
        log.info("Find cart by userId {}", userId);

        Cart cart = getOrCreateCart(userId);
        log.debug("=== CART FROM DATABASE ===");
        log.debug("Total MRP: {}, Total Selling: {}, Total Items: {}, Discount: {}",
                cart.getTotalMrpPrice(),
                cart.getTotalSellingPrice(),
                cart.getTotalItems(),
                cart.getDiscount());
        return CartMapper.toCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse.CartItemResponse addItemToCart(String userId, AddToCartItemRequest request) {
        log.info("Adding item to cart for userId {}", userId);

        String productId = request.getProductId();
        Integer quantity = request.getQuantity();

        // Validating productId and fetch info
        ProductResponse product = utils.validateAndGetProductDetails(productId, userId);

        Cart cart = getOrCreateCart(userId);

        utils.validateStockAndQuantity(product, quantity);

        CartItem cartItem = cartItemDao.findByProductIdAndUserId(productId, userId);

        /*
         * If cartItem is null means product is not added in the
         * cart before if it is already added then just need to
         * update quantity,mrpPrice and sellingPrice
         */
        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setQuantity(quantity);
            cartItem.setCartId(cart.getId());
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setMrpPrice(product.getMrpPrice());
            cartItem.setSellingPrice(product.getSellingPrice());
        } else {
            utils.updateExistingCartItem(cartItem, quantity, product);
        }

        cart.getCartItems().add(cartItem);
        utils.updateCartTotals(cart);
        cartDao.save(cart);
        CartItem saved = cartItemDao.saveCartItem(cartItem);
        log.info("Product: {} added to cart item successfullyY for userId: {}", productId, userId);
        return CartMapper.toCartItemResponse(saved);
    }

    private Cart getOrCreateCart(String userId) {

        return cartDao.findCartByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    utils.setCartTotalsToZero(newCart);
                    return cartDao.save(newCart);
                });
    }


}
