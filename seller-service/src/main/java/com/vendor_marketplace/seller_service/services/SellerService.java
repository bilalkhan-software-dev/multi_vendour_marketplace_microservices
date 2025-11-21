package com.vendor_marketplace.seller_service.services;

import com.vendor_marketplace.common.dto.event.SellerCreatedEvent;
import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.common.dto.response.SellerResponse;
import com.vendor_marketplace.seller_service.models.dto.request.UpdateSellerRequest;

import java.util.List;

public interface SellerService {

    SellerResponse registerSeller(SellerCreatedEvent sellerCreatedEvents);
    PagedResponse<SellerResponse> getAllSellers(Integer pageNo);
    void deleteSeller(Long sellerId);
    SellerResponse getSellerById(Long sellerId);
    SellerResponse updateSeller(Long seller, UpdateSellerRequest updateSellerRequest);
    boolean isSellerExist(Long id);
    boolean isSellerExist(String id);
    SellerResponse getSellerByAuthId(String id);

}
