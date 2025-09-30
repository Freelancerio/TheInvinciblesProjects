package com.outh.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class BackendApplicationTests {

    @Test
    @Timeout(value = 13, unit = TimeUnit.MINUTES)
    void contextLoads() {
        assertTrue(true, "Application context should load successfully");
    }

}