package com.vendor_marketplace.seller_service.services.impl;

import com.vendor_marketplace.common.dto.event.SellerCreatedEvent;
import com.vendor_marketplace.seller_service.dao.interfaces.SellerDao;
import com.vendor_marketplace.seller_service.exception.ExistDataException;
import com.vendor_marketplace.seller_service.exception.ResourceNotFoundException;
import com.vendor_marketplace.seller_service.models.dto.request.UpdateSellerRequest;
import com.vendor_marketplace.seller_service.models.dto.response.SellerResponse;
import com.vendor_marketplace.seller_service.models.entity.BankDetails;
import com.vendor_marketplace.seller_service.models.entity.BusinessDetails;
import com.vendor_marketplace.seller_service.models.entity.Seller;
import com.vendor_marketplace.seller_service.models.entity.SellerAddress;
import com.vendor_marketplace.common.dto.enums.AccountStatus;
import com.vendor_marketplace.seller_service.services.SellerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
class SellerServiceImpl implements SellerService {

    private final SellerDao sellerDao;


    @Override
    @Transactional
    public SellerResponse registerSeller(SellerCreatedEvent request) {
        log.info("Seller registered request with email: {} keycloak: {} ", request.getEmail(), request.getAuthId());

        validateSeller(request.getAuthId(),request.getEmail(),request.getSTRN());

        SellerCreatedEvent.SellerBankDetails bankDetails = request.getBankDetails();
        SellerCreatedEvent.SellerBusinessDetails businessDetails = request.getBusinessDetails();
        SellerCreatedEvent.SellerAddress pickupAddress = request.getPickupAddress();

        Seller seller = Seller.builder()
                .email(request.getEmail())
                .authId(request.getAuthId())
                .mobile(request.getMobile())
                .name(request.getName())
                .STRN(request.getSTRN())
                .bankDetails(BankDetails.builder()
                        .accountHolderName(bankDetails.getAccountHolderName())
                        .accountNumber(bankDetails.getAccountNumber())
                        .bankName(bankDetails.getBankName())
                        .IBAN(bankDetails.getIBAN())
                        .build())
                .businessDetails(BusinessDetails.builder()
                        .businessEmail(businessDetails.getBusinessEmail())
                        .businessName(businessDetails.getBusinessName())
                        .businessAddress(businessDetails.getBusinessAddress())
                        .banner(businessDetails.getBanner())
                        .logo(businessDetails.getLogo())
                        .businessMobileNumber(businessDetails.getBusinessMobileNumber())
                        .build())
                .pickupAddress(SellerAddress.builder()
                        .city(pickupAddress.getCity())
                        .state(pickupAddress.getState())
                        .country(pickupAddress.getCountry())
                        .postalCode(pickupAddress.getPostalCode())
                        .addressName(pickupAddress.getAddressName())
                        .locality(pickupAddress.getLocality())
                        .mobile(pickupAddress.getMobile())
                        .address(pickupAddress.getAddress() == null ? null : pickupAddress.getAddress())
                        .build())
                .build();


        Seller saved = sellerDao.saveUser(seller);
        log.info("Seller registered successfully! with email: {} auth_id: {} ", request.getEmail(), request.getAuthId());
        return buildSellerResponse(saved);

    }

    @Override
    public List<SellerResponse> getAllSellers() {

        List<Seller> allUsers = sellerDao.getAllUsers();

        return allUsers.stream()
                .map(this::buildSellerResponse
                )
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSeller(Long sellerId) {
        log.info("Deleting seller with id: {}",sellerId);
        Boolean exists = sellerDao.existsById(sellerId);
        if (!exists) {
            log.info("Deleting seller with id: {} not found",sellerId);
            throw new ResourceNotFoundException("Seller with id: " + sellerId + " not found");
        }
        sellerDao.deleteSellerById(sellerId);
        log.info("Successfully deleted seller with id: {}",sellerId);
    }

    @Override
    public SellerResponse getSellerById(Long sellerId) {

        Seller seller = sellerDao.findById(sellerId).orElseThrow(
                () -> new ResourceNotFoundException("Seller with id: " + sellerId + " not found")
        );

        return buildSellerResponse(seller);
    }

    @Override
    @Transactional
    public SellerResponse updateSeller(Long seller, UpdateSellerRequest updateSellerRequest) {
        log.info("Updating seller with id : {}",seller);

        Seller existingSeller = sellerDao.findById(seller).orElseThrow(
                () -> {
                    log.info("Seller not found with id: {}", seller);
                   return new ResourceNotFoundException("Seller not found with id: " + seller);
                }
        );
        validateSeller(existingSeller.getAuthId(),existingSeller.getEmail(),existingSeller.getSTRN());

        BeanUtils.copyProperties(updateSellerRequest, existingSeller);
        return buildSellerResponse(sellerDao.saveUser(existingSeller));
    }

    @Override
    public boolean isSellerExist(Long id){
        return sellerDao.existsById(id);
    }

    @Override
    public boolean isSellerExist(String id){
        return sellerDao.existsByAuthId(id);
    }







    private SellerResponse buildSellerResponse(Seller seller){

        BankDetails bankDetails = seller.getBankDetails();
        BusinessDetails businessDetails = seller.getBusinessDetails();
        SellerAddress pickupAddress = seller.getPickupAddress();

        return SellerResponse.builder()
                .sellerId(seller.getId())
                .name(seller.getName())
                .email(seller.getEmail())
                .authId(seller.getAuthId())
                .mobile(seller.getMobile())
                .createdAt(seller.getCreatedAt())
                .updatedAt(seller.getUpdatedAt())
                .bankDetails(bankDetails != null ?
                        SellerResponse.SellerBankDetails.builder()
                                .accountHolderName(bankDetails.getAccountHolderName())
                                .international_bank_account_number(bankDetails.getAccountNumber())
                                .accountNumber(bankDetails.getAccountNumber())
                                .bankName(bankDetails.getBankName())
                                .build() :  null)
                .businessDetails(businessDetails != null ?
                        SellerResponse.SellerBusinessDetails.builder()
                                .banner(businessDetails.getBanner())
                                .logo(businessDetails.getLogo())
                                .businessAddress(businessDetails.getBusinessAddress())
                                .businessEmail(businessDetails.getBusinessEmail())
                                .businessMobileNumber(businessDetails.getBusinessMobileNumber())
                                .businessName(businessDetails.getBusinessName())
                                .build() :  null)
                .pickupAddress(pickupAddress != null ?
                        SellerResponse.SellerAddress.builder()
                                .city(pickupAddress.getCity())
                                .state(pickupAddress.getState())
                                .country(pickupAddress.getCountry())
                                .postalCode(pickupAddress.getPostalCode())
                                .addressName(pickupAddress.getAddressName())
                                .locality(pickupAddress.getLocality())
                                .mobile(pickupAddress.getMobile())
                                .address(pickupAddress.getAddress())
                                .build() :  null)
                .build();
    }

    private void validateSeller(final String auth_id,final String email,final String sales_tax_registration_number ){
        log.info("Validating Seller registered request with email: {} keycloak: {} ", email,auth_id);
        if (auth_id == null || email == null) {
            throw new IllegalArgumentException("auth id or email is required");
        }



        Boolean isAlreadyExist = sellerDao.checkAuthIdOrEmailExist(auth_id ,email);
        if (isAlreadyExist){
            log.warn("Validating Seller registered request matches with email: {} auth_Id: {}  already exists in our record", email,auth_id);
            throw new ExistDataException(String.format("Seller already exist with email: %s or  auth_id: %s", email,auth_id));
        }

        Boolean existsByStrn = sellerDao.existsByStrn(sales_tax_registration_number);
        if (existsByStrn){
            log.warn("Validating Seller registered request matches with sales tax registration number : {} already exists in our record", sales_tax_registration_number);
            throw new ExistDataException(String.format("Seller already exist with : %s sales tax registration number",sales_tax_registration_number));
        }
    }



}
