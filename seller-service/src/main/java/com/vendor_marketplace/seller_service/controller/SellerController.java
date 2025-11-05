package com.vendor_marketplace.seller_service.controller;


import com.vendor_marketplace.seller_service.handler.GenericResponseHandler;
import com.vendor_marketplace.seller_service.models.dto.request.SellerCreatedEvent;
import com.vendor_marketplace.seller_service.models.dto.request.UpdateSellerRequest;
import com.vendor_marketplace.seller_service.models.dto.response.SellerResponse;
import com.vendor_marketplace.seller_service.models.entity.enums.AccountStatus;
import com.vendor_marketplace.seller_service.services.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;
    private final GenericResponseHandler response;

    @PostMapping("/register")
    ResponseEntity<?> registerSellerSyncMethod(@RequestBody SellerCreatedEvent request){

        SellerResponse sellerResponse = sellerService.registerSeller(request);

        if (sellerResponse != null){
            return response.createBuildResponse("Seller registered successfully!",sellerResponse, HttpStatus.CREATED);
        }
        return response.createErrorResponseMessage("Something went wrong when registering user. Please try again later",HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/update/{id}")
    ResponseEntity<?> updateSeller(@RequestBody UpdateSellerRequest request,
                                   @PathVariable Long id
                                   ){

        SellerResponse sellerResponse = sellerService.updateSeller(id, request);

        return response.createBuildResponse("Seller updated successfully!", sellerResponse, HttpStatus.OK);
    }

    @PatchMapping("/update/account/{id}")
    ResponseEntity<?> updateSellerAccountStatus(@RequestBody AccountStatus request, @PathVariable Long id){

        SellerResponse sellerResponse = sellerService.updateSellerAccountStatus(id, request);

        return response.createBuildResponse("Seller account status updated successfully!", sellerResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getSellerById(@PathVariable Long id){
        SellerResponse sellerResponse = sellerService.getSellerById(id);
        return response.createBuildResponse("Seller found successfully!", sellerResponse, HttpStatus.OK);
    }

    @GetMapping("/sellers")
    ResponseEntity<?> getAllSellers(){
        List<SellerResponse> sellerResponse = sellerService.getAllSellers();

        return response.createBuildResponse("Sellers found successfully!", sellerResponse, HttpStatus.OK);
    }

    @GetMapping("/account/status/{id}")
    ResponseEntity<?> getAccountStatusOfTheSeller(@PathVariable Long id){

        AccountStatus sellerResponse = sellerService.getSellerAccountStatus(id);

        return response.createBuildResponse("Seller account status found successfully!", sellerResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteSeller(@PathVariable Long id){
        sellerService.deleteSeller(id);
        return response.createBuildResponseMessage("Seller deleted successfully with id: "+id, HttpStatus.OK);
    }









}
