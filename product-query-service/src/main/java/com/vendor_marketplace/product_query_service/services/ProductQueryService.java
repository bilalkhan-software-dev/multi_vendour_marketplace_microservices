package com.vendor_marketplace.product_query_service.services;


import com.vendor_marketplace.common.dto.response.ProductResponse;
import com.vendor_marketplace.common.dto.response.PagedResponse;


public interface ProductQueryService {


    PagedResponse<ProductResponse> getProducts(
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



    PagedResponse<ProductResponse> similarProducts(String productId, Integer pageNo, boolean isNewest);

    PagedResponse<ProductResponse> getProductBySellerId(String sellerId, Integer pageNo, boolean isNewest);

    PagedResponse<ProductResponse> searchProducts(String queryText, Integer pageNo);

    ProductResponse getProductById(String productId);


    boolean productExistWithId(String product);
}
