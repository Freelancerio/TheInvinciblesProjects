package com.smartbet;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;

    public CustomOAuth2UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        System.out.println("OAuth2 User Service - Here");
        OAuth2User oAuth2User = super.loadUser(request);

        String provider = request.getClientRegistration().getRegistrationId();
        System.out.println("OAuth2 User Service - Provider: " + provider);

        if (!provider.equals("github")) {
            System.out.println("Not a GitHub request, skipping processing");
            return oAuth2User;
        }

        try {
            String externalId = oAuth2User.getAttribute("id").toString();
            System.out.println("GitHub User ID: " + externalId);
            System.out.println("GitHub OAuth2User attributes: " + oAuth2User.getAttributes());

            Optional<User> possibleUser = userService.findById(externalId);
            User systemUser;

            if (possibleUser.isPresent()) {
                systemUser = possibleUser.get();
                System.out.println("Found existing GitHub user: " + externalId);
            } else {
                systemUser = new User(externalId);
                userService.createUser(systemUser);
                System.out.println("Created new GitHub user: " + externalId);
            }

            return oAuth2User;
        } catch (Exception e) {
            System.err.println("Error processing GitHub OAuth user: " + e.getMessage());
            // Return the OAuth user even if database operations fail
            return oAuth2User;
        }
    }
}