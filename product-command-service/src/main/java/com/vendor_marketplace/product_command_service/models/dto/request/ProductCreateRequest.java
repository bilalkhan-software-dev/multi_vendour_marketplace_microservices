package com.vendor_marketplace.product_command_service.models.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductCreateRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 600, message = "Description must be between 10 and 600 characters")
    private String description;

    @NotBlank(message = "Primary category is required")
    private String category1;

    private String category2;

    private String category3;

    @NotNull(message = "Stocks is required")
    @Positive(message = "Stocks must be greater than 0")
    private Integer stocks;

    @NotNull(message = "Selling price is required")
    @Positive(message = "Selling price must be greater than 0")
    private Integer sellingPrice;

    @NotNull(message = "MRP price is required")
    @Positive(message = "MRP price must be greater than 0")
    @Min(value = 1, message = "MRP must be at least 1")
    private Integer mrpPrice;

    @NotEmpty(message = "At least one product image is required")
    private List<@NotBlank(message = "Image URL cannot be blank") String> images;

    @NotEmpty(message = "Color is required")
    private List<@NotBlank(message = "At least one color is required") String> colors;

    @NotEmpty(message = "Size is required")
    private List<@NotBlank(message = "At least one size is required") String> sizes;
}
