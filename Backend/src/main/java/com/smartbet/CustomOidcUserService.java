package com.smartbet;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.Optional;

@Service
public class CustomOidcUserService extends OidcUserService {
    private final UserService userService;

    public CustomOidcUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String externalId = oidcUser.getSubject();

        System.out.println("Google OIDC User - Provider: " + provider);
        System.out.println("Google OIDC User - Subject: " + externalId);
        System.out.println("Google OIDC User attributes: " + oidcUser.getAttributes());

        try {
            Optional<User> possibleUser = userService.findById(externalId);
            User systemUser;

            if (possibleUser.isPresent()) {
                systemUser = possibleUser.get();
                System.out.println("Found existing Google user: " + externalId);
            } else {
                systemUser = new User(externalId);
                userService.createUser(systemUser);
                System.out.println("Created new Google user: " + externalId);
            }

            return oidcUser;
        } catch (Exception e) {
            System.err.println("Error processing Google OIDC user: " + e.getMessage());
            // Return the OIDC user even if database operations fail
            return oidcUser;
        }
    }
}