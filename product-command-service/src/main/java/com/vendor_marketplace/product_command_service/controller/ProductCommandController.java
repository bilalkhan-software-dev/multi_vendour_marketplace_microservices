package com.vendor_marketplace.product_command_service.controller;

import com.vendor_marketplace.common.dto.response.ProductResponse;
import com.vendor_marketplace.product_command_service.handler.GenericResponseHandler;
import com.vendor_marketplace.product_command_service.models.dto.request.ProductCreateRequest;
import com.vendor_marketplace.product_command_service.models.dto.request.ProductUpdateRequest;
import com.vendor_marketplace.product_command_service.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.vendor_marketplace.common.constants.AuthHeaderConstant.CUSTOM_USER_ID_AUTHORIZATION_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/products/command")
@Tag(
        name = "Product Command",
        description = "Endpoints for sellers to manage products (create, update, delete)"
)
public class ProductCommandController {

    private final ProductService productService;
    private final GenericResponseHandler response;


    @Operation(
            summary = "Add new product",
            description = "Create a new product listing"
    )
    @PostMapping
    ResponseEntity<?> addProduct(@RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String sellerId, @Valid @RequestBody ProductCreateRequest product) {
        ProductResponse productResponse = productService.addProduct(sellerId, product);

        return response.createBuildResponse("Product added successfully", productResponse, HttpStatus.CREATED);
    }


    @Operation(
            summary = "Update product",
            description = "Update an existing product listing"
    )
    @PatchMapping("/{id}")
    ResponseEntity<?> updateProduct(@RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String sellerId, @PathVariable String id, @Valid @RequestBody ProductUpdateRequest product) {
        ProductResponse productResponse = productService.updateProduct(id, sellerId, product);

        return response.createBuildResponse("Product updated successfully", productResponse, HttpStatus.OK);
    }


    @Operation(
            summary = "Delete product",
            description = "Delete a product"
    )
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteProduct(@RequestHeader(CUSTOM_USER_ID_AUTHORIZATION_HEADER) String sellerId, @PathVariable String id) {
        productService.deleteProductById(sellerId, id);
        return response.createBuildResponseMessage("Product deleted successfully", HttpStatus.OK);
    }


}
