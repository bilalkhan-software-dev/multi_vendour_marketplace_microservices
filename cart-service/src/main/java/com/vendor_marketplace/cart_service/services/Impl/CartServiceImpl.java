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

        // Check if product already exists in user's cart
        if (cartItem == null) {
            // NEW ITEM: Create fresh cart item
            cartItem = new CartItem();
            cartItem.setQuantity(quantity);
            cartItem.setCartId(cart.getId());
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setMrpPrice(quantity * product.getMrpPrice());
            cartItem.setSellingPrice(quantity * product.getSellingPrice());
            cartItem.setProductSellerId(product.getSellerId());

            //  Add to cart's collection for cascade to work
            cart.getCartItems().add(cartItem);
        } else {
            // EXISTING ITEM: Update quantity and prices
            utils.updateExistingCartItem(cartItem, quantity, product);
        }

        utils.updateCartTotals(cart);
        cartDao.save(cart);
        log.info("Product: {} added to cart item successfullyY for userId: {}", productId, userId);
        return CartMapper.toCartItemResponse(cartItem);
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
