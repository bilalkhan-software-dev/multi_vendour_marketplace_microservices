package com.vendor_marketplace.cart_service.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "product-query-service")
public interface ThirdPartyProductService {

    @GetMapping("/api/v2/products/query/{id}")
    ResponseEntity<Map<String,Object>> getProductDetails(@PathVariable String id);



}
