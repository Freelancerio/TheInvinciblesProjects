package com.smartbet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            System.out.println("Authentication failed: " + (exception != null ? exception.getMessage() : "Unknown error"));

            String errorParam = "auth_failed";
            if (exception != null) {
                // Log the actual exception for debugging
                System.out.println("Exception type: " + exception.getClass().getSimpleName());
                System.out.println("Exception message: " + exception.getMessage());
            }

            try {
                response.sendRedirect("https://the-invincibles-projects.vercel.app/login?error=" + errorParam);
            } catch (IOException e) {
                System.err.println("Failed to redirect after auth failure: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        };
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            System.out.println("Authentication successful for user: " + authentication.getName());
            try {
                response.sendRedirect("https://the-invincibles-projects.vercel.app/userDashboard");
            } catch (IOException e) {
                System.err.println("Failed to redirect after successful auth: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        };
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/login", "/error", "/webjars/**", "/css/**", "/js/**").permitAll()
                                .requestMatchers("/api/database/**").permitAll()
                                .requestMatchers("/api/database/psl/**").permitAll()
                                .requestMatchers("/health").permitAll()
                                .requestMatchers("/actuator/health").permitAll()
                                .requestMatchers("/register", "/auth/**").permitAll()
                                .requestMatchers("/user-info", "/api/**").authenticated()
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("https://the-invincibles-projects.vercel.app/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(authenticationSuccessHandler())
                        .failureHandler(authenticationFailureHandler())
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            System.out.println("Authentication required for: " + request.getRequestURI());
                            try {
                                response.sendRedirect("https://the-invincibles-projects.vercel.app/login?error=session_expired");
                            } catch (IOException e) {
                                System.err.println("Failed to redirect to login: " + e.getMessage());
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            }
                        })
                );
        return http.build();
    }
}