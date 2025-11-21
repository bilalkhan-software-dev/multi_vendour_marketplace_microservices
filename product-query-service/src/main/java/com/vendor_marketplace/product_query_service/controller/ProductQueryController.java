package com.vendor_marketplace.product_query_service.controller;

import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.common.dto.response.ProductResponse;
import com.vendor_marketplace.product_query_service.handler.GenericResponseHandler;
import com.vendor_marketplace.product_query_service.services.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/products/query")
public class ProductQueryController {

    private final ProductQueryService productService;
    private final GenericResponseHandler response;

    @GetMapping("")
    ResponseEntity<?> getProductsFilter(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String colors,
            @RequestParam(required = false) String sizes,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minDiscount,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String stock,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber
    ){

        PagedResponse<ProductResponse> products = productService.getProducts(title, category, brand, colors, sizes, minPrice, maxPrice, minDiscount, sort, stock, pageNumber);

        if (!products.getContent().isEmpty()) {
            return response.createBuildResponse("Products retrieved successfully", products, HttpStatus.OK);
        }
        return response.createBuildResponse("No products found matching your criteria", products, HttpStatus.OK);
    }

    @GetMapping("/similar")
    ResponseEntity<?> getSimilarProducts(
            @RequestParam String productId,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "true") boolean isNewest){

        PagedResponse<ProductResponse> productResponsePagedResponse = productService.similarProducts(productId, pageNumber, isNewest);

        if (!productResponsePagedResponse.getContent().isEmpty()) {
            return response.createBuildResponse("Similar products found", productResponsePagedResponse, HttpStatus.OK);
        }
        return response.createBuildResponse("No similar products available at the moment", productResponsePagedResponse, HttpStatus.OK);
    }

    @GetMapping("/seller")
    ResponseEntity<?> getSellerProducts(
            @RequestParam String sellerId,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "true") boolean isNewest){

        PagedResponse<ProductResponse> productResponsePagedResponse = productService.getProductBySellerId(sellerId, pageNumber, isNewest);

        if (!productResponsePagedResponse.getContent().isEmpty()) {
            return response.createBuildResponse("Seller products retrieved successfully", productResponsePagedResponse, HttpStatus.OK);
        }
        return response.createBuildResponse("This seller currently has no products available", productResponsePagedResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getProductById(@PathVariable String id){

        ProductResponse productById = productService.getProductById(id);

        return response.createBuildResponse("Product details retrieved successfully", productById, HttpStatus.OK);
    }

    @GetMapping("/search")
    ResponseEntity<?> searchProduct(
            @RequestParam String queryText,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber
    ){
        PagedResponse<ProductResponse> productResponsePagedResponse = productService.searchProducts(queryText, pageNumber);

        if (!productResponsePagedResponse.getContent().isEmpty()) {
            return response.createBuildResponse("Search completed successfully", productResponsePagedResponse, HttpStatus.OK);
        }
        return response.createBuildResponse("No products found for your search query", productResponsePagedResponse, HttpStatus.OK);
    }
}