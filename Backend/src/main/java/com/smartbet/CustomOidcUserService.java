package com.smartbet;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends OidcUserService {
    private final UserService userService;

    public CustomOidcUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String externalId = oidcUser.getSubject();

        System.out.println("Google OIDC User - Provider: " + provider);
        System.out.println("Google OIDC User - Subject: " + externalId);
        System.out.println("Google OIDC User attributes: " + oidcUser.getAttributes());

        Optional<User> possibleUser = userService.findById(externalId);
        User systemUser;

        if (possibleUser.isPresent()){
            systemUser = possibleUser.get();
        }
        else{
            systemUser = new User(externalId);
            userService.createUser(systemUser);
        }

        // userService.findById(externalId).orElseGet(() -> {
        //     User newUser = new User(externalId);
        //     return userService.createUser(newUser);
        // });

        return oidcUser;
    }
}