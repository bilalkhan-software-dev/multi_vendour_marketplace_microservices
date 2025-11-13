package com.vendor_marketplace.seller_service.services;

import com.vendor_marketplace.common.dto.event.SellerCreatedEvent;
import com.vendor_marketplace.seller_service.models.dto.request.UpdateSellerRequest;
import com.vendor_marketplace.seller_service.models.dto.response.SellerResponse;

import java.util.List;

public interface SellerService {

    SellerResponse registerSeller(SellerCreatedEvent sellerCreatedEvents);

    List<SellerResponse> getAllSellers();

    void deleteSeller(Long sellerId);

    SellerResponse getSellerById(Long sellerId);
    SellerResponse updateSeller(Long seller, UpdateSellerRequest updateSellerRequest);

    boolean isSellerExist(Long id);

    boolean isSellerExist(String id);









}
