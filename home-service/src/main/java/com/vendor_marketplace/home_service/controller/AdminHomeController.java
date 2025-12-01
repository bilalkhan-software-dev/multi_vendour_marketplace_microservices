package com.vendor_marketplace.home_service.controller;

import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.home_service.handler.GenericResponseHandler;
import com.vendor_marketplace.home_service.models.dto.request.HomeCategoryRequest;
import com.vendor_marketplace.home_service.models.dto.request.UpdateHomeCategoryRequest;
import com.vendor_marketplace.home_service.models.dto.response.Home;
import com.vendor_marketplace.home_service.models.dto.response.HomeCategoryResponse;
import com.vendor_marketplace.home_service.models.entity.enums.HomeCategorySection;
import com.vendor_marketplace.home_service.services.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/admin/home")
@Tag(
        name = "Admin Home Categories",
        description = "Admin endpoints for managing home page categories"
)
public class AdminHomeController {

    private final HomeService homeService;
    private final GenericResponseHandler response;

    @Operation(
            summary = "Create home category",
            description = "Create a new home page category"
    )
    @PostMapping
    ResponseEntity<?> createHomeCategory(@Valid @RequestBody HomeCategoryRequest request) {

        HomeCategoryResponse homeCategory = homeService.createHomeCategory(request);

        return response.createBuildResponse("Home category added successfully", homeCategory, HttpStatus.CREATED);

    }

    @Operation(
            summary = "Create bulk home categories",
            description = "Create multiple home page categories in bulk (max 50)"
    )
    @PostMapping("/bulk")
    ResponseEntity<?> createHomeCategories(
            @Valid
            @NotEmpty(message = "At least one category is required")
            @RequestBody List<HomeCategoryRequest> request
    ) {

        if (request.size() > 50) {
            return response.createBuildResponseMessage("Too many categories", HttpStatus.BAD_REQUEST);
        }

        Home homeCategory = homeService.createHomeCategories(request);
        return response.createBuildResponse("Home categories processed successfully", homeCategory, HttpStatus.CREATED);

    }

    @Operation(
            summary = "Update home category",
            description = "Update an existing home page category"
    )
    @PatchMapping("/{id}")
    ResponseEntity<?> updateHomeCategory(
            @Parameter(
                    description = "Category ID",
                    required = true,
                    example = "65a1b2c3d4e5f67890123456"
            )
            @PathVariable @NotBlank(message = "ID is required") String id,
            @RequestBody UpdateHomeCategoryRequest request) {

        HomeCategoryResponse homeCategory = homeService.updateHomeCategory(id, request);

        return response.createBuildResponse("Home category updated successfully", homeCategory, HttpStatus.OK);

    }

    @Operation(
            summary = "Delete home category",
            description = "Delete a home page category"
    )
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteHomeCategory(
            @Parameter(
                    description = "Category ID",
                    required = true,
                    example = "65a1b2c3d4e5f67890123456"
            )
            @PathVariable @NotBlank(message = "ID is required") String id) {

        homeService.deleteHomeCategory(id);

        return response.createBuildResponseMessage("Home category deleted successfully", HttpStatus.OK);

    }

    @Operation(
            summary = "Get categories by section",
            description = "Get paginated home categories by section type"
    )
    @GetMapping("/section")
    public ResponseEntity<?> getHomeCategoryBySection(
            @Parameter(
                    description = "Page number (zero-based)",
                    example = "0"
            )
            @RequestParam(required = false, defaultValue = "0") int page,

            @Parameter(
                    description = "Number of items per page (max 100)",
                    example = "10"
            )
            @RequestParam(required = false, defaultValue = "10")
            @Max(value = 100, message = "Size must be less than or equal to 100") int size,

            @Parameter(
                    description = "Sort by newest first",
                    example = "true"
            )
            @RequestParam(required = false, defaultValue = "true") boolean isNewest,

            @Parameter(
                    description = "Section type filter",
                    required = true,
                    example = "FEATURED"
            )
            @RequestParam @NotNull(message = "Section is required") HomeCategorySection section
    ) {
        PagedResponse<HomeCategoryResponse> homeCategoriesBySection = homeService.getHomeCategoriesBySection(section, page, size, isNewest);
        return response.createBuildResponse("Home categories retrieved successfully", homeCategoriesBySection, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all categories",
            description = "Get paginated list of all home categories"
    )
    @GetMapping
    public ResponseEntity<?> getHomeCategory(
            @Parameter(
                    description = "Page number (zero-based)",
                    example = "0"
            )
            @RequestParam(required = false, defaultValue = "0") int page,

            @Parameter(
                    description = "Number of items per page (max 100)",
                    example = "10"
            )
            @RequestParam(required = false, defaultValue = "10")
            @Max(value = 100, message = "Size must be less than or equal to 100") int size,

            @Parameter(
                    description = "Sort by newest first",
                    example = "true"
            )
            @RequestParam(required = false, defaultValue = "true") boolean isNewest
    ) {
        PagedResponse<HomeCategoryResponse> homeCategoriesBySection = homeService.getAllHomeCategories(page, size, isNewest);
        return response.createBuildResponse("Home categories retrieved successfully", homeCategoriesBySection, HttpStatus.OK);
    }

}