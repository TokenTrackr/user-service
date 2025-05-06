package com.tokentrackr.user_service.service.impl;

import com.tokentrackr.user_service.dto.request.CreateUserRequest;
import com.tokentrackr.user_service.model.User;
import com.tokentrackr.user_service.service.interfaces.KeycloakService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class KeycloakImpl implements KeycloakService {
    private final WebClient webClient;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.realm}")
    private String realm;

    private final ClientRegistration keycloakRegistration;
    private final OAuth2AuthorizedClientManager authorizedClientManager;

    public KeycloakImpl(
            WebClient webClient,
            @Value("${keycloak.realm:tokentrackr-dev}") String realm,
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientManager authorizedClientManager
    ) {
        this.webClient = webClient;
        this.realm = realm;
        this.keycloakRegistration = clientRegistrationRepository.findByRegistrationId("keycloak");
        this.authorizedClientManager = authorizedClientManager;
    }

    private String getAdminAccessToken() {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("keycloak")
                .principal(adminUsername)
                .build();

        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);
        if (authorizedClient != null) {
            return authorizedClient.getAccessToken().getTokenValue();
        } else {
            throw new RuntimeException("Failed to obtain admin access token");
        }
    }
    @Override
    public String createUser(CreateUserRequest request) {
        // 1. Obtain a fresh admin token
        String adminToken = getAdminAccessToken();

        // 2. Build the Keycloak user representation
        var representation = new HashMap<String, Object>();
        representation.put("username", request.getUsername());
        representation.put("enabled", true);
        representation.put("credentials", List.of(
                Map.of(
                        "type", "password",
                        "value", request.getPassword(),
                        "temporary", false
                )
        ));

        // 3. Call Keycloak Admin API to create the user, passing the Bearer token
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/admin/realms/{realm}/users")
                        .build(realm)
                )
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .bodyValue(representation)
                .retrieve()
                .toBodilessEntity()
                .map(response -> {
                    var location = response.getHeaders().getLocation();
                    if (location != null) {
                        String path = location.getPath();
                        return path.substring(path.lastIndexOf('/') + 1);
                    }
                    log.error("Keycloak response has no Location header: {}", response);
                    throw new RuntimeException("Failed to obtain created user ID from Keycloak");
                })
                .block();
    }

    @Override
    public User getUserById(String userId) {
        try {
            return webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/admin/realms/{realm}/users/{userId}")
                            .build(realm, userId))
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
        } catch (WebClientResponseException e) {
            // Handle response error (e.g., 404 if user not found)
            throw new RuntimeException("Error fetching user from Keycloak: " + e.getMessage(), e);
        }
    }
}
