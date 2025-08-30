package com.smartbet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity//enables wev security support
@EnableMethodSecurity //allows you to put security rules directly on methods in your services,controllers, or repositories
// e.g.  @PreAuthorize("hasRole('ADMIN')")
//    public void deleteAccount(Long id) {
//        // only admins can delete accounts
//    }

public class SecurityConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService)
                ).defaultSuccessUrl("http://localhost:3000/profile",true)); // change link to https://the-invincibles-projects.vercel.app/profile
                //or http://localhost:3000/profile depeding if need for production or local
        return http.build();
    }

}
