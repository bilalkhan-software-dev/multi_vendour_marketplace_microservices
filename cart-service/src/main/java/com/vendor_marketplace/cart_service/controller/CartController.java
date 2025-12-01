package com.vendor_marketplace.cart_service.controller;

import com.vendor_marketplace.cart_service.handler.GenericResponseHandler;
import com.vendor_marketplace.cart_service.models.dto.request.AddToCartItemRequest;
import com.vendor_marketplace.cart_service.models.dto.request.UpdateCartItemRequest;
import com.vendor_marketplace.cart_service.services.CartItemService;
import com.vendor_marketplace.cart_service.services.CartService;
import com.vendor_marketplace.common.dto.response.CartResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Cart Management",
        description = "Endpoints for managing shopping carts and cart items"
)
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final GenericResponseHandler response;

    @Operation(
            summary = "Get user cart",
            description = "Retrieve the current user's shopping cart with all items"
    )
    @GetMapping
    public ResponseEntity<?> getUserCart(
            @Parameter(
                    description = "User ID from authentication header",
                    required = true,
                    hidden = true
            )
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId) {
        CartResponse userCart = cartService.getUserCart(userId);
        return response.createBuildResponse("Your cart retrieved successfully", userCart, HttpStatus.OK);
    }

    @Operation(
            summary = "Add item to cart",
            description = "Add a new product/item to the user's shopping cart"
    )
    @PostMapping("/items")
    public ResponseEntity<?> addItemToCart(
            @Parameter(
                    description = "User ID from authentication header",
                    required = true,
                    hidden = true
            )
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId,
            @Valid @RequestBody AddToCartItemRequest request) {

        CartResponse.CartItemResponse cartItemResponse = cartService.addItemToCart(userId, request);
        return response.createBuildResponse("Product added successfully to cart", cartItemResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Update cart item",
            description = "Update quantity or other details of a specific item in the cart"
    )
    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<?> updateCartItem(
            @Parameter(
                    description = "User ID from authentication header",
                    required = true,
                    hidden = true
            )
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId,
            @Parameter(
                    description = "ID of the cart item to update",
                    required = true,
                    example = "12345"
            )
            @PathVariable @NotNull(message = "CartItem id is required") Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request) {

        CartResponse.CartItemResponse cartItemResponse = cartItemService.updateCartItem(userId, cartItemId, request);
        return response.createBuildResponse("Cart item updated successfully", cartItemResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Remove item from cart",
            description = "Remove a specific item from the shopping cart"
    )
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<?> removeItemFromCart(
            @Parameter(
                    description = "User ID from authentication header",
                    required = true,
                    hidden = true
            )
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId,
            @Parameter(
                    description = "ID of the cart item to remove",
                    required = true,
                    example = "12345"
            )
            @PathVariable @NotNull(message = "CartItem id is required") Long cartItemId) {

        cartItemService.removeCartItem(userId, cartItemId);
        return response.createBuildResponseMessage("Cart item removed successfully", HttpStatus.OK);
    }

    @Operation(
            summary = "Clear cart",
            description = "Remove all items from the user's shopping cart"
    )
    @DeleteMapping
    public ResponseEntity<?> clearCart(
            @Parameter(
                    description = "User ID from authentication header",
                    required = true,
                    hidden = true
            )
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId) {
        cartItemService.clearUserCart(userId);
        return response.createBuildResponseMessage("Cart cleared successfully", HttpStatus.OK);
    }

    @Operation(
            summary = "Get cart items",
            description = "Retrieve all items from a specific cart"
    )
    @GetMapping("/{cartId}/items")
    public ResponseEntity<?> getCartItems(
            @Parameter(
                    description = "User ID from authentication header",
                    required = true,
                    hidden = true
            )
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId,
            @Parameter(
                    description = "Cart ID",
                    required = true,
                    example = "cart_12345"
            )
            @PathVariable @NotNull(message = "Cart id is required") String cartId) {

        List<CartResponse.CartItemResponse> items = cartItemService.getCartItemsByUserOrCartId(userId, cartId);
        return response.createBuildResponse("Cart items retrieved successfully", items, HttpStatus.OK);
    }

    @Operation(
            summary = "Get cart item by ID",
            description = "Retrieve a specific cart item by its ID"
    )
    @GetMapping("/items/{cartItemId}")
    public ResponseEntity<?> getCartItemById(
            @Parameter(
                    description = "Cart item ID",
                    required = true,
                    example = "12345"
            )
            @PathVariable @NotNull(message = "CartItem id is required") Long cartItemId) {
        CartResponse.CartItemResponse item = cartItemService.findCartItemById(cartItemId);
        return response.createBuildResponse("Cart item retrieved successfully", item, HttpStatus.OK);
    }
}