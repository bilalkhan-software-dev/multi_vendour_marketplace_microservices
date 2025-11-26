package com.vendor_marketplace.order_service.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;


import java.util.Map;

import static com.vendor_marketplace.common.constants.AuthHeaderConstant.CUSTOM_USER_ID_AUTHORIZATION_HEADER;

@FeignClient(
        name = "cart-service",
        url = "${feign.client.cart-service.url:http://CART-SERVICE}"
)
public interface CartServiceClient {

    @GetMapping("/api/v2/carts")
    ResponseEntity<Map<String,Object>> getUserCart(@RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String userId);

}
