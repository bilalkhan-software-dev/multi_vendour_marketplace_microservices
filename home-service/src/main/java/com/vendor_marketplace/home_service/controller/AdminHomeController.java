package com.vendor_marketplace.home_service.controller;


import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.home_service.handler.GenericResponseHandler;
import com.vendor_marketplace.home_service.models.dto.request.HomeCategoryRequest;
import com.vendor_marketplace.home_service.models.dto.request.UpdateHomeCategoryRequest;
import com.vendor_marketplace.home_service.models.dto.response.Home;
import com.vendor_marketplace.home_service.models.dto.response.HomeCategoryResponse;
import com.vendor_marketplace.home_service.models.entity.enums.HomeCategorySection;
import com.vendor_marketplace.home_service.services.HomeService;
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
public class AdminHomeController {

    private final HomeService homeService;
    private final GenericResponseHandler response;

    @PostMapping
    ResponseEntity<?> createHomeCategory(@Valid @RequestBody HomeCategoryRequest request) {

        HomeCategoryResponse homeCategory = homeService.createHomeCategory(request);

        return response.createBuildResponse("Home category added successfully", homeCategory, HttpStatus.CREATED);

    }


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


    @PatchMapping("/{id}")
    ResponseEntity<?> updateHomeCategory(@PathVariable @NotBlank(message = "ID is required") String id, @RequestBody UpdateHomeCategoryRequest request) {

        HomeCategoryResponse homeCategory = homeService.updateHomeCategory(id, request);

        return response.createBuildResponse("Home category updated successfully", homeCategory, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteHomeCategory(@PathVariable @NotBlank(message = "ID is required") String id) {

        homeService.deleteHomeCategory(id);

        return response.createBuildResponseMessage("Home category deleted successfully", HttpStatus.OK);

    }


    @GetMapping("/section")
    public ResponseEntity<?> getHomeCategoryBySection(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10")
            @Max(value = 100, message = "Size must be less than or equal to 100") int size,
            @RequestParam(required = false, defaultValue = "true") boolean isNewest,
            @RequestParam @NotNull(message = "Section is required") HomeCategorySection section
    ) {
        PagedResponse<HomeCategoryResponse> homeCategoriesBySection = homeService.getHomeCategoriesBySection(section, page, size, isNewest);
        return response.createBuildResponse("Home categories retrieved successfully", homeCategoriesBySection, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<?> getHomeCategory(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10")
            @Max(value = 100, message = "Size must be less than or equal to 100") int size,
            @RequestParam(required = false, defaultValue = "true") boolean isNewest
    ) {
        PagedResponse<HomeCategoryResponse> homeCategoriesBySection = homeService.getAllHomeCategories(page, size, isNewest);
        return response.createBuildResponse("Home categories retrieved successfully", homeCategoriesBySection, HttpStatus.OK);
    }


}
