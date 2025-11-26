package com.vendor_marketplace.review_wishlist_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ReviewWishlistServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewWishlistServiceApplication.class, args);
	}

}
