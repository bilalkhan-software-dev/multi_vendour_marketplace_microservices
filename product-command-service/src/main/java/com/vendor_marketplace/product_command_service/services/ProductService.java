package com.vendor_marketplace.product_command_service.services;

import com.vendor_marketplace.common.dto.response.ProductResponse;
import com.vendor_marketplace.product_command_service.models.dto.request.ProductCreateRequest;
import com.vendor_marketplace.product_command_service.models.dto.request.ProductUpdateRequest;

public interface ProductService {

    ProductResponse addProduct(String authUserSellerId, ProductCreateRequest request);

    void deleteProductById(String authUserSellerId, String productId);

    ProductResponse updateProduct(String productId, String authUserSellerId, ProductUpdateRequest productUpdateRequest);

    void updateStocks(String productId, int quantity);
}
