package com.vendor_marketplace.cart_service.controller;

import com.vendor_marketplace.cart_service.handler.GenericResponseHandler;
import com.vendor_marketplace.cart_service.models.dto.request.AddToCartItemRequest;
import com.vendor_marketplace.cart_service.models.dto.request.UpdateCartItemRequest;
import com.vendor_marketplace.cart_service.services.CartItemService;
import com.vendor_marketplace.cart_service.services.CartService;
import com.vendor_marketplace.common.dto.response.CartResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.vendor_marketplace.common.constants.AuthHeaderConstant.CUSTOM_USER_ID_AUTHORIZATION_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/carts")
@Validated
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final GenericResponseHandler response;

    @GetMapping
    public ResponseEntity<?> getUserCart(@RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId) {
        CartResponse userCart = cartService.getUserCart(userId);
        return response.createBuildResponse("Your cart retrieved successfully", userCart, HttpStatus.OK);
    }

    @PostMapping("/items")
    public ResponseEntity<?> addItemToCart(
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId,
            @Valid @RequestBody AddToCartItemRequest request) {

        CartResponse.CartItemResponse cartItemResponse = cartService.addItemToCart(userId, request);
        return response.createBuildResponse("Product added successfully to cart", cartItemResponse, HttpStatus.OK);
    }

    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<?> updateCartItem(
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId,
            @PathVariable @NotNull(message = "CartItem id is required") Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request) {

        CartResponse.CartItemResponse cartItemResponse = cartItemService.updateCartItem(userId, cartItemId, request);
        return response.createBuildResponse("Cart item updated successfully", cartItemResponse, HttpStatus.OK);
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<?> removeItemFromCart(
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId,
            @PathVariable @NotNull(message = "CartItem id is required") Long cartItemId) {

        cartItemService.removeCartItem(userId, cartItemId);
        return response.createBuildResponseMessage("Cart item removed successfully", HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> clearCart(@RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId) {
        cartItemService.clearUserCart(userId);
        return response.createBuildResponseMessage("Cart cleared successfully", HttpStatus.OK);
    }

    @GetMapping("/{cartId}/items")
    public ResponseEntity<?> getCartItems(
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId,
            @PathVariable @NotNull(message = "Cart id is required") String cartId) {

        List<CartResponse.CartItemResponse> items = cartItemService.getCartItemsByUserOrCartId(userId, cartId);
        return response.createBuildResponse("Cart items retrieved successfully", items, HttpStatus.OK);
    }

    @GetMapping("/items/{cartItemId}")
    public ResponseEntity<?> getCartItemById(@PathVariable @NotNull(message = "CartItem id is required") Long cartItemId) {
        CartResponse.CartItemResponse item = cartItemService.findCartItemById(cartItemId);
        return response.createBuildResponse("Cart item retrieved successfully", item, HttpStatus.OK);
    }
}