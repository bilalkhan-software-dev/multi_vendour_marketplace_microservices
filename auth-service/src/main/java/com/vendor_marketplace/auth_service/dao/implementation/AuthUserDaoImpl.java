package com.vendor_marketplace.auth_service.dao.implementation;

import com.vendor_marketplace.auth_service.dao.interfaces.AuthUserDao;
import com.vendor_marketplace.auth_service.dao.repository.AuthUserRepository;
import com.vendor_marketplace.auth_service.models.entity.AuthUser;
import com.vendor_marketplace.common.dto.enums.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class AuthUserDaoImpl implements AuthUserDao {

    private final AuthUserRepository authRepository;

    @Override
    public Optional<AuthUser> findByEmail(String email) {
        return authRepository.findByEmail(email);
    }

    @Override
    public Optional<AuthUser> findById(String id) {
        return authRepository.findById(UUID.fromString(id));
    }

    @Override
    public boolean existsByEmail(String email) {
        return authRepository.existsByEmail(email);
    }

    @Override
    public boolean existsBySalesTaxRegistrationNumber(String strn){
        return authRepository.existsBySalesTaxRegistrationNumber(strn);
    }

    @Override
    public boolean existsById(String id) {
        return authRepository.existsById(UUID.fromString(id));
    }

    @Override
    public AuthUser save(AuthUser authUser) {
        return authRepository.save(authUser);
    }

    @Override
    public void deleteAuthUser(String id) {
        authRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public List<AuthUser> findAll(int size, int page, String direction) {
        Sort sort = direction != null ? Sort.by(direction) : Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AuthUser> authUsers = authRepository.findAll(pageable);
        return authUsers.getContent();
    }

    @Override
    public AccountStatus getAccountStatus(String id) {
        return authRepository.getSellerAccountStatus(id);
    }
}
