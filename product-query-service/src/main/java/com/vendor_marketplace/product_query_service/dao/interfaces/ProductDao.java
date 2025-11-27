package com.vendor_marketplace.product_query_service.dao.interfaces;


import com.vendor_marketplace.common.dto.response.ProductResponse;
import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.product_query_service.models.entity.Category;
import com.vendor_marketplace.product_query_service.models.entity.Product;

import java.util.Optional;

public interface ProductDao {


    PagedResponse<ProductResponse> findProducts(
            String title,
            String category,
            String brand,
            String colors,
            String sizes,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber
    );

    PagedResponse<ProductResponse> searchProducts(String text, Integer pageNumber);

    Optional<Product> findByProductId(String productId);


    Optional<Category> findByCategoryId(String categoryId);

    Product save(Product product);

    Category saveCategory(Category category);

    void deleteByProductId(String id);

    PagedResponse<ProductResponse> findSimilarProducts(String productId, int pageNumber, boolean isNewest);

    PagedResponse<ProductResponse> findSellerProducts(String sellerId, int pageNo, boolean isNewest);

    boolean existByProductId(String productId);
}
