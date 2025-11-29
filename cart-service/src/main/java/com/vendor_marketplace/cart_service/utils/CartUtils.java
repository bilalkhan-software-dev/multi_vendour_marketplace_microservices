package com.vendor_marketplace.cart_service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vendor_marketplace.cart_service.feignClient.ThirdPartyProductService;
import com.vendor_marketplace.cart_service.models.entity.Cart;
import com.vendor_marketplace.cart_service.models.entity.CartItem;
import com.vendor_marketplace.common.dto.response.ProductResponse;
import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.common.exception.UnsufficientStockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.vendor_marketplace.common.utils.ProductUtil.calculateDiscountPercentage;

@RequiredArgsConstructor
@Component
@Slf4j
public class CartUtils {

    private final ThirdPartyProductService productService;
    private final ObjectMapper mapper;

    public void validateStockAndQuantity(ProductResponse product, int quantity) {

        if (product.getStocks() <= 0) {
            throw new UnsufficientStockException("Product is out of stock");
        }

        if (quantity > product.getStocks()) {
            throw new UnsufficientStockException("Quantity is out of stock");
        }
        if (quantity <= 0) {
            throw new UnsufficientStockException("Quantity must be greater than zero");
        }
    }

    public ProductResponse validateAndGetProductDetails(String productId, String userId) {
        log.info("Verifying Product | productId={} for userId: {}", productId, userId);
        ResponseEntity<Map<String, Object>> response = productService.getProductDetails(productId);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.info("Product: {} varification failed  statusCode:{}", productId, response.getStatusCode());
            throw new ResourceNotFoundException("Product not found");
        }

        Map<String, Object> body = response.getBody();
        if (body == null || body.get("data") == null) {
            log.info("Product: {} body is null", productId);
            throw new ResourceNotFoundException("Product not found");
        }


        ProductResponse productResponse = mapper.convertValue(body.get("data"), ProductResponse.class);

        log.info("Product validated | productId: {} for userId={}", productId, userId);

        return productResponse;
    }

    public void updateExistingCartItem(CartItem cartItem, int quantity, ProductResponse product) {
        int newQuantity = cartItem.getQuantity() + quantity;

        validateStockAndQuantity(product, newQuantity);

        int newSellingPrice = newQuantity * product.getSellingPrice();
        int newMrpPrice = newQuantity * product.getMrpPrice();

        cartItem.setQuantity(newQuantity);
        cartItem.setSellingPrice(newSellingPrice);
        cartItem.setMrpPrice(newMrpPrice);
    }

    public void updateCartTotals(Cart cart) {


//        int totalMrpPrice = 0;
//        int totalSellingPrice = 0;
//        int totalItems = 0;
//
//        for (CartItem cartItem : cart.getCartItems()) {
//            totalMrpPrice += cartItem.getMrpPrice();
//            totalSellingPrice += cartItem.getSellingPrice();
//            totalItems += cartItem.getQuantity();
//        }

        // Functional style but less performance if you have less amount of data
        int totalItems = cart.getCartItems().stream().mapToInt(CartItem::getQuantity).sum();
        int totalMrpPrice = cart.getCartItems().stream().mapToInt(CartItem::getMrpPrice).sum();
        int totalSellingPrice = cart.getCartItems().stream().mapToInt(CartItem::getSellingPrice).sum();

        cart.setTotalItems(totalItems);
        cart.setTotalMrpPrice(totalMrpPrice);
        cart.setTotalSellingPrice(totalSellingPrice);
        cart.setDiscount(calculateDiscountPercentage(totalMrpPrice, totalSellingPrice));

    }

    public void setCartTotalsToZero(Cart cart) {
        cart.setTotalItems(0);
        cart.setTotalMrpPrice(0);
        cart.setTotalSellingPrice(0);
        cart.setDiscount(0.0);
    }


}
