package com.vendor_marketplace.product_command_service.dao.repository;

import com.vendor_marketplace.product_command_service.models.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product,String> {

    Optional<Product> findByProductId(String productId);

    void deleteByProductId(String productId);
}
