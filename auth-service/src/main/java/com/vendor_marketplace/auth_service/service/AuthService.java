package com.vendor_marketplace.auth_service.service;

import com.vendor_marketplace.auth_service.dao.interfaces.AuthUserDao;
import com.vendor_marketplace.auth_service.exception.AuthenticationException;
import com.vendor_marketplace.auth_service.exception.ExistDataException;
import com.vendor_marketplace.auth_service.exception.ResourceNotFoundException;
import com.vendor_marketplace.common.helper.EmailSendingTemplate;
import com.vendor_marketplace.auth_service.mapper.AuthUserMapper;
import com.vendor_marketplace.auth_service.models.dto.request.LoginRequest;
import com.vendor_marketplace.auth_service.models.dto.request.SellerRegisterRequest;
import com.vendor_marketplace.auth_service.models.dto.request.UserRegisterRequest;
import com.vendor_marketplace.common.dto.enums.AccountStatus;
import com.vendor_marketplace.common.dto.enums.USER_ROLE;
import com.vendor_marketplace.auth_service.models.dto.response.AuthUserResponse;
import com.vendor_marketplace.auth_service.models.dto.response.LoginResponse;
import com.vendor_marketplace.auth_service.models.entity.AuthUser;
import com.vendor_marketplace.auth_service.utils.JwtUtil;
import com.vendor_marketplace.auth_service.utils.RandomUtil;
import com.vendor_marketplace.auth_service.utils.RedisUtil;
import com.vendor_marketplace.common.dto.event.SellerCreatedEvent;
import com.vendor_marketplace.common.dto.event.SendOTPEvent;
import com.vendor_marketplace.common.dto.event.UserCreatedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final KafkaPublisher kafkaPublisher;
    private final AuthUserDao authUserDao;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;

    @Transactional
    public String registerUser(UserRegisterRequest request) {
        log.info("Registering new user: {}", request.getEmail());

        String authId = createAndGetAuthId(request.getEmail(), request.getFullName(), USER_ROLE.ROLE_CUSTOMER);

        UserCreatedEvent event = UserCreatedEvent.builder()
                .authId(authId)
                .email(request.getEmail())
                .fullName(request.getFullName())
                .mobile(request.getMobile())
                .build();

        // Publish user created event
        kafkaPublisher.publishUserCreatedEvent(event);

        log.info("User registered successfully: {}", request.getEmail());
        return authId;
    }


    @Transactional
    public String registerSeller(SellerRegisterRequest request) {
        log.info("Registering new seller: {}", request.getEmail());
        validateSeller(request.getSTRN());
        String authId = createAndGetAuthId(request.getEmail(), request.getName(), USER_ROLE.ROLE_SELLER);



        SellerCreatedEvent event = SellerCreatedEvent.builder()
                .name(request.getName())
                .mobile(request.getMobile())
                .authId(authId)
                .email(request.getEmail())
                .bankDetails(SellerCreatedEvent.SellerBankDetails.builder()
                        .accountNumber(request.getBankDetails().getAccountNumber())
                        .bankName(request.getBankDetails().getBankName())
                        .accountHolderName(request.getBankDetails().getAccountHolderName())
                        .IBAN(request.getBankDetails().getIBAN())
                        .build())
                .businessDetails(SellerCreatedEvent.SellerBusinessDetails.builder()
                        .businessName(request.getBusinessDetails().getBusinessName())
                        .businessAddress(request.getBusinessDetails().getBusinessAddress())
                        .businessMobileNumber(request.getBusinessDetails().getBusinessMobileNumber())
                        .businessEmail(request.getBusinessDetails().getBusinessEmail())
                        .banner(request.getBusinessDetails().getBanner())
                        .logo(request.getBusinessDetails().getLogo())
                        .build())
                .pickupAddress(SellerCreatedEvent.SellerAddress.builder()
                        .addressName(request.getPickupAddress().getAddressName())
                        .locality(request.getPickupAddress().getLocality())
                        .city(request.getPickupAddress().getCity())
                        .state(request.getPickupAddress().getState())
                        .postalCode(request.getPickupAddress().getPostalCode())
                        .country(request.getPickupAddress().getCountry())
                        .mobile(request.getPickupAddress().getCountry())
                        .address(request.getPickupAddress().getAddress())
                        .build())
                .STRN(request.getSTRN())
                .build();

        // Publish seller created event
        kafkaPublisher.publishSellerCreatedEvent(event);

        log.info("Seller registered successfully: {}", request.getEmail());
        return authId;
    }

    public LoginResponse login(LoginRequest request) {

        AuthUser existUser = authUserDao.findByEmail(request.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + request.getEmail())
        );

        validateAccountStatus(existUser);

        String cacheKey = RedisUtil.user(existUser.getEmail());
        String otp = redisUtil.get(cacheKey, String.class);
        if (otp == null) {
            throw new ResourceNotFoundException(String.format("OTP not found with: %s. Generate OTP again maybe its expires ", request.getEmail()));
        }

        if (!otp.equals(request.getOtp())) {
            throw new AuthenticationException("Invalid OTP");
        }

        String generatedToken = jwtUtil.generateToken(existUser.getFullName(), existUser.getEmail(), existUser.getId().toString(), existUser.getRole().name());

        return LoginResponse.builder()
                .token(generatedToken)
                .build();

    }




    public void sendOTPForLogin(String email) {
        log.info("Request accepted Sending OTP for login: {}", email);
        AuthUser authUser = authUserDao.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + email)
        );

        validateAccountStatus(authUser);

        String cacheKey = RedisUtil.user(email);

        if (redisUtil.get(cacheKey, String.class) != null) {
            redisUtil.deleteFromRedis(cacheKey);
        }

        String generateOtp = RandomUtil.toGenerateOtp();
        log.info("OTP for login: {}", generateOtp);
        redisUtil.saveToRedis(cacheKey, generateOtp);

        String body = EmailSendingTemplate.sendEmailForOTP(authUser.getFullName(), generateOtp);

        SendOTPEvent event = SendOTPEvent.builder()
                .to(email)
                .subject("Vendor Marketplace OTP verification")
                .body(body)
                .build();

        kafkaPublisher.publishSendOTPEvent(event);
    }

    public AuthUserResponse getAuthUserById(String id) {
        return AuthUserMapper.toAuthUserResponse(authUserDao.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + id)
        ));
    }

    public void deleteAuthUserById(String id) {
        authUserDao.deleteAuthUser(id);
    }

    public List<AuthUserResponse> allAuthUsers(int page, int size, String sortBy) {
        List<AuthUser> users = authUserDao.findAll(size, page, sortBy);
        return users.stream().map(AuthUserMapper::toAuthUserResponse).toList();
    }

    public boolean validateEmail(String email) {
        return authUserDao.existsByEmail(email);
    }

    @Transactional
    public AuthUserResponse updateSellerAccountStatus(String id, AccountStatus accountStatus) {

        log.info("Updating account status with id: {}", id);


        AuthUser existing = authUserDao.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with id: " + id + " not found")
        );

        if (existing.getRole().equals(USER_ROLE.ROLE_ADMIN)) {
            log.info("Admin cannot be banned");
            throw new AuthenticationException("Admin account cannot be banned or deactivate. You cannot perform this operation");
        }


        if (existing.getAccountStatus().equals(accountStatus)) {
            log.info("Same account status detected. Don't need to update seller account");
            return AuthUserMapper.toAuthUserResponse(existing);
        }

        existing.setAccountStatus(accountStatus);

        AuthUser updated = authUserDao.save(existing);
        log.info("Successfully updated account status with id: {}", id);
        return AuthUserMapper.toAuthUserResponse(updated);
    }

    public AccountStatus getAccountStatus(String id) {

        boolean exists = authUserDao.existsById(id);
        if (!exists) {
            throw new ResourceNotFoundException("Seller with id: " + id + " not found");
        }

        return authUserDao.getAccountStatus(id);
    }


    private String createAndGetAuthId(String email, String name, USER_ROLE role) {


        boolean exists = authUserDao.existsByEmail(email);
        if (exists) {
            throw new ExistDataException("Email already exist in our record");
        }


        AuthUser authUser = AuthUser.builder()
                .email(email)
                .role(role)
                .fullName(name)
                .accountStatus(AccountStatus.ACTIVE)
                .build();
        return authUserDao.save(authUser).getId().toString();
    }

    private void validateSeller(String strn) {
        log.info("Validating seller sales tax registration number: {} ",strn);
        if (authUserDao.existsBySalesTaxRegistrationNumber(strn)) {
            log.warn("Seller with sales tax registration number: {} already exists", strn);
            throw new ExistDataException("Sales Tax Registration already in use");
        }
    }

    private void validateAccountStatus(AuthUser existUser) {
        AccountStatus accountStatus = existUser.getAccountStatus();
        switch (accountStatus) {
            case SUSPENDED -> throw new AuthenticationException("Your Account is Suspended.");
            case BANNED -> throw new AuthenticationException("Your Account is Banned.");
            case CLOSED -> throw new AuthenticationException("Your Account is Closed.");
            case DEACTIVATED -> throw new AuthenticationException("Your Account is Deactivated.");
        }
    }
}