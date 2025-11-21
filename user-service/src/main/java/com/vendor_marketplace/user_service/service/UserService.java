package com.vendor_marketplace.user_service.service;


import com.vendor_marketplace.common.dto.event.UserCreatedEvent;
import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.common.exception.ExistDataException;
import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.user_service.dao.interfaces.UserDao;
import com.vendor_marketplace.user_service.dao.repository.UserRepository;
import com.vendor_marketplace.user_service.models.dto.request.AddressRequest;
import com.vendor_marketplace.user_service.models.dto.request.UpdateUserRequest;
import com.vendor_marketplace.user_service.models.dto.response.UserResponse;
import com.vendor_marketplace.user_service.models.entity.Address;
import com.vendor_marketplace.user_service.models.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    private final UserDao userDao;

    @Transactional
    public UserResponse registerUser(UserCreatedEvent request) {
        log.info("Registering User with : {}", request.getEmail());
        Boolean emailExist = userDao.checkAuthIdOrEmailExist(request.getAuthId(),request.getEmail());

        if (emailExist) {
            log.warn("Registering user already exist : {}", request.getEmail());
            throw new ExistDataException(String.format("User with email: %s or auth_id: %s already exist", request.getEmail(),request.getAuthId()));
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .authId(request.getAuthId())
                .mobile(request.getMobile())
                .build();

        User saved = userDao.saveUser(user);
        return buildUserResponse(saved);
    }



    public UserResponse getUserById(Long id) {
        log.info("Fetching User with id : {}", id);
        return buildUserResponse(userDao.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User with id %s not found", id))
        ));
    }

    @Transactional
    public void deleteUser(Long userId){
        log.info("Deleting user with id : {}", userId);
        Boolean exists = userDao.existsById(userId);
        if (exists) {
            userDao.deleteUserById(userId);
            log.info("Deleted successfully user with id : {}", userId);
        }else {
            log.warn("User with id : {} not found", userId);
            throw new ResourceNotFoundException(String.format("User with id %s not found", userId));
        }
    }

    @Transactional
    public UserResponse addAddressToUser(Long userId, AddressRequest request) {
        log.info("Adding address to User with id : {}", userId);
        User user = userDao.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Address address = toEntity(request);
        user.addAddress(address);
        User updatedUser = userRepository.save(user);
        log.info("Address added in user with id : {}", updatedUser.getId());

        return buildUserResponse(updatedUser);
    }


    public Set<UserResponse.UserAddress> getUserAddress(Long userId){

        List<Address> addressesByUserId = userDao.getAddressesByUserId(userId);

       return addressesByUserId.stream().map(this::buildUserAddressResponse).collect(Collectors.toSet());
    }

    @Transactional
    public UserResponse updateUser(UpdateUserRequest request){

      User existingUser = userDao.findById(request.getId()).orElseThrow(
              () -> new ResourceNotFoundException(String.format("User with id %s not found", request.getId()))
      );
      existingUser.setFullName(request.getFullName());
    return buildUserResponse(userDao.saveUser(existingUser));

    }


    public boolean isUserExistWithById(Long id){
        return userDao.existsById(id);
    }

    public boolean isUserExistWithById(String id){
        return userDao.existsByAuthId(id);
    }

    public PagedResponse<UserResponse> getAllUsers(Integer pageNo){


        Page<User> allUsers = userDao.getAllUsers(pageNo);

        List<UserResponse> users = allUsers.getContent().stream().map(this::buildUserResponse).toList();

        return PagedResponse.<UserResponse>builder()
                .content(users)
                .totalPages(allUsers.getTotalPages())
                .totalElements(allUsers.getTotalElements())
                .pageNumber(allUsers.getNumber())
                .pageSize(allUsers.getSize())
                .isFirstPage(allUsers.isFirst())
                .isLastPage(allUsers.isLast())
                .build();


    }

    private UserResponse buildUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .authId(user.getAuthId())
                .mobile(user.getMobile() != null ? user.getMobile() : "")
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .addresses(user.getAddresses().stream().map(this::buildUserAddressResponse)
                        .collect(Collectors.toSet()))
                .build();
    }

    private UserResponse.UserAddress buildUserAddressResponse(Address address){
        return UserResponse.UserAddress.builder()
                .userId(address.getUser().getId())
                .name(address.getName())
                .city(address.getCity())
                .state(address.getState())
                .pinCode(address.getPinCode())
                .mobile(address.getMobile())
                .id(address.getId())
                .locality(address.getLocality())
                .createdAt(address.getCreatedAt())
                .updatedAt(address.getUpdatedAt())
                .build();
    }



    private Address toEntity(AddressRequest request){
        return Address.builder()
                .name(request.getName())
                .city(request.getCity())
                .state(request.getState())
                .pinCode(request.getPinCode())
                .mobile(request.getMobile())
                .locality(request.getLocality())
                .build();
    }


}
