package com.vendor_marketplace.product_command_service.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "seller-service",url = "http://localhost:8081/api/v2/seller")
public interface ThirdPartySellerService {

    @GetMapping("/{id}")
    ResponseEntity<Map<String,Object>> getSellerById(@PathVariable String id);

}
