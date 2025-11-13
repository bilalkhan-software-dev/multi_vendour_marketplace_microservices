package com.vendor_marketplace.auth_service.initializer;

import com.vendor_marketplace.auth_service.dao.interfaces.AuthUserDao;
import com.vendor_marketplace.auth_service.models.entity.AuthUser;
import com.vendor_marketplace.auth_service.service.KafkaPublisher;
import com.vendor_marketplace.common.dto.enums.AccountStatus;
import com.vendor_marketplace.common.dto.enums.USER_ROLE;
import com.vendor_marketplace.common.dto.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements CommandLineRunner {

    private final AuthUserDao authUserDao;
    private final KafkaPublisher kafkaPublisher;

    @Override
    public void run(String... args) {
        String adminEmail = "noorjisuper@gmail.com";

        // Check if admin already exists
        if (authUserDao.findByEmail(adminEmail).isPresent()) {
            log.info("Admin already exists. Skipping creation and event publishing.");
            return;
        }

        // Create admin user
        AuthUser authUser = AuthUser.builder()
                .accountStatus(AccountStatus.ACTIVE)
                .role(USER_ROLE.ROLE_ADMIN)
                .email(adminEmail)
                .fullName("Admin Vendor Marketplace")
                .build();

        AuthUser savedUser = authUserDao.save(authUser);
        log.info("🟢 Created admin user with ID: {}", savedUser.getId());

        // Publish user created event
        UserCreatedEvent event = UserCreatedEvent.builder()
                .authId(savedUser.getId().toString())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .mobile("03001234567")
                .build();

        kafkaPublisher.publishUserCreatedEvent(event);
        log.info("Published UserCreatedEvent for admin: {}", savedUser.getEmail());
    }
}
