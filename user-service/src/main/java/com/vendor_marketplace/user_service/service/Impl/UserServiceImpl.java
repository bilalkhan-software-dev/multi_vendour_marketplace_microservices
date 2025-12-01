package com.vendor_marketplace.user_service.service.Impl;

import com.vendor_marketplace.common.dto.event.UserCreatedEvent;
import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.common.exception.ExistDataException;
import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.user_service.dao.interfaces.UserDao;
import com.vendor_marketplace.user_service.models.dto.request.AddressRequest;
import com.vendor_marketplace.user_service.models.dto.request.UpdateUserRequest;
import com.vendor_marketplace.user_service.models.dto.response.UserAddressResponse;
import com.vendor_marketplace.user_service.models.dto.response.UserResponse;
import com.vendor_marketplace.user_service.models.entity.Address;
import com.vendor_marketplace.user_service.models.entity.User;
import com.vendor_marketplace.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Transactional
    @Override
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
        return UserService.buildUserResponse(saved);
    }



    @Override
    public UserResponse getUserById(Long id) {
        log.info("Fetching User with id : {}", id);
        return UserService.buildUserResponse(userDao.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User with id %s not found", id))
        ));
    }

    @Transactional
    @Override
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
    @Override
    public UserResponse addAddressToUser(Long userId, AddressRequest request) {
        log.info("Adding address to User with id : {}", userId);
        User user = userDao.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Address address = UserService.toEntity(request);
        user.addAddress(address);
        User updatedUser = userDao.saveUser(user);
        log.info("Address added in user with id : {}", updatedUser.getId());

        return UserService.buildUserResponse(updatedUser);
    }


    @Override
    public Set<UserAddressResponse> getUserAddress(Long userId){

        List<Address> addressesByUserId = userDao.getAddressesByUserId(userId);

        return addressesByUserId.stream().map(UserService::buildUserAddressResponse).collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public UserResponse updateUser(UpdateUserRequest request){

        User existingUser = userDao.findById(request.getId()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("User with id %s not found", request.getId()))
        );
        existingUser.setFullName(request.getFullName());
        return UserService.buildUserResponse(userDao.saveUser(existingUser));

    }

    @Override
    public boolean isUserExistWithById(Long id){
        return userDao.existsById(id);
    }

    @Override
    public boolean isUserExistWithById(String id){
        return userDao.existsByAuthId(id);
    }

    @Override
    public PagedResponse<UserResponse> getAllUsers(Integer pageNo){


        Page<User> allUsers = userDao.getAllUsers(pageNo);

        List<UserResponse> users = allUsers.getContent().stream().map(UserService::buildUserResponse).toList();

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

    @Override
    public UserAddressResponse getAddressById(Long id) {
        return userDao.getAddressById(id);
    }
}
