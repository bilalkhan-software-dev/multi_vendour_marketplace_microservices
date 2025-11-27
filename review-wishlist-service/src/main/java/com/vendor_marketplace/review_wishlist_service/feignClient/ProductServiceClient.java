package com.vendor_marketplace.review_wishlist_service.feignClient;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-query-service",
        url = "${feign.client.cart-service.url:http://PRODUCT-QUERY-SERVICE}")
public interface ProductServiceClient {

    @GetMapping("/api/v2/products/query/{id}/exist")
    ResponseEntity<@NonNull Boolean> getProductExist(@PathVariable String id);


}
