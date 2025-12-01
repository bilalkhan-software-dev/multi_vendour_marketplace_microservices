package com.vendor_marketplace.product_query_service.controller;

import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.common.dto.response.ProductResponse;
import com.vendor_marketplace.product_query_service.handler.GenericResponseHandler;
import com.vendor_marketplace.product_query_service.services.ProductQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/products/query")
@Tag(
        name = "Product Query",
        description = "Endpoints for querying and searching products"
)
public class ProductQueryController {

    private final ProductQueryService productService;
    private final GenericResponseHandler response;

    @Operation(
            summary = "Get products with filters",
            description = "Retrieve products with various filtering options"
    )
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

            @Parameter(description = "Sort order (low_to_high, high_to_low, newest, oldest)", example = "newest")
            @RequestParam(required = false) String sort,

            @Parameter(description = "Stock availability filter (in_stock, out_of_stock)", example = "in_stock")
            @RequestParam(required = false) String stock,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber
    ) {

        PagedResponse<ProductResponse> products = productService.getProducts(title, category, brand, colors, sizes, minPrice, maxPrice, minDiscount, sort, stock, pageNumber);

        if (!products.getContent().isEmpty()) {
            return response.createBuildResponse("Products retrieved successfully", products, HttpStatus.OK);
        }
        return response.createBuildResponse("No products found matching your criteria", products, HttpStatus.OK);
    }

    @Operation(
            summary = "Get similar products",
            description = "Find products similar to a given product"
    )
    @GetMapping("/similar")
    ResponseEntity<?> getSimilarProducts(
            @Parameter(description = "Product ID to find similar products for", required = true, example = "65a1b2c3d4e5f67890123456")
            @RequestParam String productId,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "true") boolean isNewest) {

        PagedResponse<ProductResponse> productResponsePagedResponse = productService.similarProducts(productId, pageNumber, isNewest);

        if (!productResponsePagedResponse.getContent().isEmpty()) {
            return response.createBuildResponse("Similar products found", productResponsePagedResponse, HttpStatus.OK);
        }
        return response.createBuildResponse("No similar products available at the moment", productResponsePagedResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Get seller products",
            description = "Retrieve all products from a specific seller"
    )
    @GetMapping("/seller")
    ResponseEntity<?> getSellerProducts(
            @RequestParam String sellerId,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "true") boolean isNewest) {

        PagedResponse<ProductResponse> productResponsePagedResponse = productService.getProductBySellerId(sellerId, pageNumber, isNewest);

        if (!productResponsePagedResponse.getContent().isEmpty()) {
            return response.createBuildResponse("Seller products retrieved successfully", productResponsePagedResponse, HttpStatus.OK);
        }
        return response.createBuildResponse("This seller currently has no products available", productResponsePagedResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Get product by ID",
            description = "Retrieve detailed product information by product ID"
    )
    @GetMapping("/{id}")
    ResponseEntity<?> getProductById(@PathVariable String id) {

        ProductResponse productById = productService.getProductById(id);

        return response.createBuildResponse("Product details retrieved successfully", productById, HttpStatus.OK);
    }

    @Operation(
            summary = "Check product existence",
            description = "Verify if a product exists by ID"
    )
    @GetMapping("/{id}/exist")
    ResponseEntity<Boolean> getProductExist(@PathVariable String id) {

        return ResponseEntity.ok(productService.productExistWithId(id));
    }

    @Operation(
            summary = "Search products",
            description = "Search products by text query"
    )
    @GetMapping("/search")
    ResponseEntity<?> searchProduct(
            @Parameter(description = "Search query text", required = true, example = "wireless headphones")
            @RequestParam String queryText,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber
    ) {
        PagedResponse<ProductResponse> productResponsePagedResponse = productService.searchProducts(queryText, pageNumber);

        if (!productResponsePagedResponse.getContent().isEmpty()) {
            return response.createBuildResponse("Search completed successfully", productResponsePagedResponse, HttpStatus.OK);
        }
        return response.createBuildResponse("No products found for your search query", productResponsePagedResponse, HttpStatus.OK);
    }
}