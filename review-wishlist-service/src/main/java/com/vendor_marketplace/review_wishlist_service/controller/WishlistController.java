package com.vendor_marketplace.review_wishlist_service.controller;

import com.vendor_marketplace.review_wishlist_service.handler.GenericResponseHandler;
import com.vendor_marketplace.review_wishlist_service.models.dto.request.AddProductToWishlist;
import com.vendor_marketplace.review_wishlist_service.models.dto.response.WishlistResponse;
import com.vendor_marketplace.review_wishlist_service.services.WishlistService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.vendor_marketplace.common.constants.AuthHeaderConstant.CUSTOM_USER_ID_AUTHORIZATION_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/wishlists/user")
public class WishlistController {

    private final WishlistService wishlistService;
    private final GenericResponseHandler response;

    @PostMapping("")
    ResponseEntity<?> addReview(@RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId, @Valid @RequestBody AddProductToWishlist request) {

        WishlistResponse added = wishlistService.addProductToWishlist(userId, request);

        return response.createBuildResponse("Product successfully added to your wishlist!", added, HttpStatus.OK);

    }

    @GetMapping("/")
    ResponseEntity<?> getUserWishlist(
            @RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId) {

        WishlistResponse wishlist = wishlistService.getWishlist(userId);

        return response.createBuildResponse("Your wishlist retrieved successfully!", wishlist, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getUserReviews(
            @PathVariable @NotNull(message = "Wishlist Id is required") Long id
    ) {

        WishlistResponse wishlist = wishlistService.getWishlistById(id);

        return response.createBuildResponse("Wishlist retrieved successfully!", wishlist, HttpStatus.OK);
    }


}
