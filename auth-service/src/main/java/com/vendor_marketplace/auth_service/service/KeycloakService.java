package com.vendor_marketplace.auth_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.account.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakService {

    private final Keycloak keycloakAdminClient;

    private  String realm;

    public boolean isEmailExists(String email) {
        try {
            List<UserRepresentation> users = keycloakAdminClient.realm(realm)
                    .users()
                    .searchByEmail(email, true);

            return !users.isEmpty();
        } catch (Exception e) {
            log.error("Error checking email existence in Keycloak: {}", email, e);
            throw new KeycloakOperationException("Failed to check email availability");
        }
    }

    public String createUser(String email, String password, String fullName, String role) {
        // 1. Check if email already exists
        if (isEmailExists(email)) {
            throw new DuplicateEmailException("Email already registered: " + email);
        }

        // 2. Create user in Keycloak
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(email);
        user.setEmail(email);
        user.setFirstName(fullName);
        user.setEmailVerified(false);

        // Create user
        Response response = keycloakAdminClient.realm(realm).users().create(user);

        if (response.getStatus() != 201) {
            throw new KeycloakOperationException("Failed to create user in Keycloak");
        }

        // 3. Get user ID from response
        String userId = extractUserIdFromResponse(response);

        // 4. Set password
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        keycloakAdminClient.realm(realm).users().get(userId).resetPassword(credential);

        // 5. Assign role
        assignRoleToUser(userId, role);

        return userId;
    }

    private String extractUserIdFromResponse(Response response) {
        String location = response.getHeaderString("Location");
        return location.substring(location.lastIndexOf('/') + 1);
    }

    private void assignRoleToUser(String userId, String role) {
        RoleRepresentation clientRole = keycloakAdminClient.realm(realm)
                .roles()
                .get(role)
                .toRepresentation();

        keycloakAdminClient.realm(realm)
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(Arrays.asList(clientRole));
    }
}