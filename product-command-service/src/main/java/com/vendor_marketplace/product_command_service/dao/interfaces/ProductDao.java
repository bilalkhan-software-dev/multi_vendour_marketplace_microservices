package com.vendor_marketplace.product_command_service.dao.interfaces;

import com.vendor_marketplace.product_command_service.models.entity.Category;
import com.vendor_marketplace.product_command_service.models.entity.Product;

import java.util.Optional;

public interface ProductDao {


    Optional<Product> findByProductId(String productId);


    Optional<Category> findByCategoryId(String categoryId);

    Product save(Product product);

    Category saveCategory(Category category);

    void deleteByProductId(String id);
}
