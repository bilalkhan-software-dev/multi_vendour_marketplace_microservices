package com.vendor_marketplace.home_service.controller;

import com.vendor_marketplace.home_service.handler.GenericResponseHandler;
import com.vendor_marketplace.home_service.models.dto.response.Home;
import com.vendor_marketplace.home_service.services.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/public/home")
@Tag(
        name = "Public Home",
        description = "Public endpoints for home page data"
)
public class PublicHomeController {

    private final HomeService homeService;
    private final GenericResponseHandler response;

    @Operation(
            summary = "Get home page data",
            description = "Retrieve home page data including categories, featured products, etc."
    )
    @GetMapping("/")
    ResponseEntity<?> getHomePageData() {

        Home categories = homeService.getHomeCategories();

        return response.createBuildResponse("Home page data retrieved successfully", categories, HttpStatus.OK);
    }

}