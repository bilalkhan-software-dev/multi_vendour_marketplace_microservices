package com.vendor_marketplace.product_query_service.dao.repository;

import com.vendor_marketplace.product_query_service.models.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product,String> {

    Optional<Product> findByProductId(String productId);

    void deleteByProductId(String productId);

    Page<Product> findBySellerId(String sellerId, Pageable pageable);


    Page<Product> findByCategory_IdAndProductIdNot(String categoryId, String productId,Pageable pageable);
}
