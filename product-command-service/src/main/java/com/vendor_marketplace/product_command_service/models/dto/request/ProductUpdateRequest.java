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
public class ProductUpdateRequest {

    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @Positive(message = "Stocks must be greater than 0")
    private Integer stocks;

    @Positive(message = "Selling price must be greater than 0")
    private Integer sellingPrice;

    @Positive(message = "MRP price must be greater than 0")
    @Min(value = 1, message = "MRP must be at least 1")
    private Integer mrpPrice;

    private List<@NotBlank(message = "Image URL cannot be blank") String> images;
    private List<@NotBlank(message = "Color cannot be blank or empty") String> colors;
    private List<@NotBlank(message = "Sizes cannot be blank or empty") String> sizes;
}
