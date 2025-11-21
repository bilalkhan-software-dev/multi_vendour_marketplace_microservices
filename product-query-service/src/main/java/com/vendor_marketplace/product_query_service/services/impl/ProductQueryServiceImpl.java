package com.vendor_marketplace.product_query_service.services.impl;

import com.vendor_marketplace.common.dto.response.ProductResponse;
import com.vendor_marketplace.product_query_service.dao.interfaces.ProductDao;
import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.product_query_service.mapper.ProductMapper;
import com.vendor_marketplace.product_query_service.services.ProductQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductDao productDao;

    @Override
    public PagedResponse<ProductResponse> getProducts(
            String title, String category,final String brand,
            String colors, String sizes,
            Integer minPrice, Integer maxPrice, Integer minDiscount,
            String sort, String stock,
            Integer pageNumber
    ) {

        return productDao.findProducts(
                title, category, brand,
                colors, sizes,
                minPrice, maxPrice, minDiscount,
                sort, stock,
                pageNumber
        );

    }


    @Override
    public PagedResponse<ProductResponse> similarProducts(final String productId, Integer pageNo, boolean isNewest) {
        return productDao.findSimilarProducts(productId, pageNo, isNewest);
    }

    @Override
    public PagedResponse<ProductResponse> getProductBySellerId(final String sellerId, Integer pageNo, boolean isNewest) {
        return productDao.findSellerProducts(sellerId, pageNo, isNewest);
    }

    @Override
    public PagedResponse<ProductResponse> searchProducts(String queryText, Integer pageNo) {
        return productDao.searchProducts(queryText, pageNo);
    }

    @Override
    public ProductResponse getProductById(final String productId) {
        return ProductMapper.toProductResponse(
                productDao.findByProductId(productId).orElseThrow(
                        () -> new ResourceNotFoundException("Product not found with id: " + productId))
        );
    }
}
