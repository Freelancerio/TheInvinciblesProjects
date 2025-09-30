package com.outh.backend.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;

@TestConfiguration
@Profile("test")
public class TestConfig {

    @Bean
    @Primary
    public RestTemplate restTemplate() {
        return mock(RestTemplate.class);
    }

    @Bean
    @Primary
    public FirebaseConfig firebaseConfig() {
        return new FirebaseConfig() {
            @Override
            public void init() {

            }
        };
    }
}