package com.vendor_marketplace.order_service.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        url = "${feign.client.user-service.url:http://USER-SERVICE}"
)
public interface UserServiceClient {

    @GetMapping("/api/v2/user/{id}/address")
    ResponseEntity<UserAddressResponse> getAddressById(@PathVariable("id") Long id);

}