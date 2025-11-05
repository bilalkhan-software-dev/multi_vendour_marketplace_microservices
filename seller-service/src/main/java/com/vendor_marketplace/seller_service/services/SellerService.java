package com.vendor_marketplace.seller_service.services;

import com.vendor_marketplace.seller_service.models.dto.request.SellerCreatedEvent;
import com.vendor_marketplace.seller_service.models.dto.request.UpdateSellerRequest;
import com.vendor_marketplace.seller_service.models.dto.response.SellerResponse;
import com.vendor_marketplace.seller_service.models.entity.enums.AccountStatus;

import java.util.List;

public interface SellerService {

    SellerResponse registerSeller(SellerCreatedEvent sellerCreatedEvents);

    List<SellerResponse> getAllSellers();

    void deleteSeller(Long sellerId);

    SellerResponse getSellerById(Long sellerId);
    SellerResponse updateSeller(Long seller, UpdateSellerRequest updateSellerRequest);

    AccountStatus getSellerAccountStatus(Long sellerId);

    SellerResponse updateSellerAccountStatus(Long sellerId, AccountStatus accountStatus);







}
