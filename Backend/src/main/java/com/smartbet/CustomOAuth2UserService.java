package com.smartbet;

import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;

    public CustomOAuth2UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        System.out.println("OAuth2 User Service - Here");
        OAuth2User oAuth2User = super.loadUser(request);

        String provider = request.getClientRegistration().getRegistrationId();
        System.out.println("OAuth2 User Service - Provider: " + provider);

        if (!provider.equals("github")) {
            System.out.println("Not a GitHub request, skipping processing");
            return oAuth2User;
        }

        String externalId = oAuth2User.getAttribute("id").toString();
        System.out.println("GitHub User ID: " + externalId);
        System.out.println("GitHub OAuth2User attributes: " + oAuth2User.getAttributes());

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

        return oAuth2User;
    }
}