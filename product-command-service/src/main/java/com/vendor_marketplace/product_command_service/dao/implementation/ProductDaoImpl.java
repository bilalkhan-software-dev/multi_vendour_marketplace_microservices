package com.vendor_marketplace.product_command_service.dao.implementation;

import com.vendor_marketplace.product_command_service.dao.interfaces.ProductDao;
import com.vendor_marketplace.product_command_service.dao.repository.CategoryRepository;
import com.vendor_marketplace.product_command_service.dao.repository.ProductRepository;
import com.vendor_marketplace.product_command_service.models.entity.Category;
import com.vendor_marketplace.product_command_service.models.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
class ProductDaoImpl implements ProductDao {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Optional<Product> findByProductId(String productId) {
        return productRepository.findByProductId(productId);
    }

    @Override
    public Optional<Category> findByCategoryId(String categoryId) {
        return categoryRepository.findByCategoryId((categoryId));
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteByProductId(String id) {
        productRepository.deleteByProductId(id);
    }














}
