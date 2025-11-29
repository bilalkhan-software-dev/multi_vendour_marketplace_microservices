package com.vendor_marketplace.product_query_service.dao.repository;

import com.vendor_marketplace.product_query_service.models.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface CategoryRepository extends MongoRepository<Category,String> {

    Optional<Category> findByCategoryIdIgnoreCase(String categoryId);
    Optional<Category> findByCategoryId(String categoryId);

    List<Category> findByNameContainingIgnoreCase(String name);








}
