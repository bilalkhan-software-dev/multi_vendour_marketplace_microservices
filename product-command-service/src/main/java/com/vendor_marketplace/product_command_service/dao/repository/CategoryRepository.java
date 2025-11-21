package com.vendor_marketplace.product_command_service.dao.repository;

import com.vendor_marketplace.product_command_service.models.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface CategoryRepository extends MongoRepository<Category,String> {

    Optional<Category> findByCategoryId(String categoryId);






}
